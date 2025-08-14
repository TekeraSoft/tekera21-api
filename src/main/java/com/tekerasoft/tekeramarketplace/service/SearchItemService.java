package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItemType;
import com.tekerasoft.tekeramarketplace.repository.esrepository.SearchItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchItemService {

    private final SearchItemRepository searchItemRepository;

    public SearchItemService(SearchItemRepository searchItemRepository) {
        this.searchItemRepository = searchItemRepository;
    }

    public void createIndex(SearchItem searchItem) {
        searchItemRepository.save(searchItem);
    }
    public void deleteItem(String id) {
            searchItemRepository.deleteById(id);
    }

    public List<SearchItem> searchProductOrCompany(String input) {
        List<SearchItem> all = searchItemRepository.findByNameAndSlugMatch(input);

        List<SearchItem> categories = all.stream()
                .filter(i -> i.getItemType() == SearchItemType.CATEGORY)
                .toList();

        List<SearchItem> products = all.stream()
                .filter(i -> i.getItemType() == SearchItemType.PRODUCT)
                .limit(10)
                .toList();

        List<SearchItem> result = new ArrayList<>();
        result.addAll(categories);
        result.addAll(products);
        return result;
    }

}
