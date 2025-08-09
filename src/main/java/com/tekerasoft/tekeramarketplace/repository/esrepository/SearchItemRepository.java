package com.tekerasoft.tekeramarketplace.repository.esrepository;

import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchItemRepository extends ElasticsearchRepository<SearchItem, String> {

    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    List<SearchItem> customAutocompleteSearch(String input);

}
