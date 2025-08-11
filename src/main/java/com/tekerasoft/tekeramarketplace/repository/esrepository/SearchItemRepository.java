package com.tekerasoft.tekeramarketplace.repository.esrepository;

import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchItemRepository extends ElasticsearchRepository<SearchItem, String> {

    @Query("{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}]}}")
    List<SearchItem> findByNameMatch(String searchTerm);

}
