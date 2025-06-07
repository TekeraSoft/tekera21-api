package com.tekerasoft.tekeramarketplace.repository.esrepository;

import com.tekerasoft.tekeramarketplace.model.esdocument.Item;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"brand\": \"?1\"}}]}}")
    List<Item> searchByNameAndBrand(String name, String brand);
}
