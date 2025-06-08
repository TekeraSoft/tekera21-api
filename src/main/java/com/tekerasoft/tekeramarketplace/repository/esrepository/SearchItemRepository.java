package com.tekerasoft.tekeramarketplace.repository.esrepository;

import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchItemRepository extends ElasticsearchRepository<SearchItem, String> {


}
