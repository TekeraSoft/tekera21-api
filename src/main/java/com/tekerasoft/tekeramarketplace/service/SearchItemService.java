package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.repository.esrepository.SearchItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return searchItemRepository.customAutocompleteSearch(input);
    }

}
