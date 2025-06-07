package com.tekerasoft.tekeramarketplace.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.tekerasoft.tekeramarketplace.dto.request.EsSearchRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.esdocument.Item;
import com.tekerasoft.tekeramarketplace.repository.esrepository.ItemRepository;
import com.tekerasoft.tekeramarketplace.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final JsonDataService jsonDataService;
    private final ElasticsearchClient elasticsearchClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    public ItemService(ItemRepository itemRepository, JsonDataService jsonDataService, ElasticsearchClient elasticsearchClient) {
        this.itemRepository = itemRepository;
        this.jsonDataService = jsonDataService;
        this.elasticsearchClient = elasticsearchClient;
    }

    public Item createIndex(Item item) {
        return itemRepository.save(item);
    }

    public void addItemsFromJson() {
        LOGGER.info("Adding items from json");
        List<Item> itemList = jsonDataService.readItemsFromJson();
        itemRepository.saveAll(itemList);
    }

    public ApiResponse<?> deleteIndex(String id) {
        itemRepository.deleteById(id);
        return new ApiResponse<>("Deleted Item", HttpStatus.OK.value());
    }

    public List<Item> getAllDataFromIndex(String indexName) {;
       try {
           return elasticsearchClient.search(
                   q -> q.index(indexName).query(EsUtil.createMatchAllQuery()).size(1000),Item.class)
                   .hits()
                   .hits()
                   .stream().map(Hit::source)
                   .collect(Collectors.toList());
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
    }

    public List<Item> searchItemsByFieldAndValue(EsSearchRequest req) {
        Supplier<Query> query = EsUtil.buildQueryForFieldAndValue(
                req.getFieldName().get(0),
                req.getSearchValue().get(0));
        try {
            return elasticsearchClient.search(q -> q.index("items_index").query(query.get()), Item.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> searchItemsByNameAndBrandWithQuery(String name, String brand) {
        return itemRepository.searchByNameAndBrand(name,brand);
    }

    public List<Item> boolQuery(EsSearchRequest req) {
        try {
            return elasticsearchClient.search(q -> q.index("items_index")
                            .query(EsUtil.createBoolQuery(req).get()),Item.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
