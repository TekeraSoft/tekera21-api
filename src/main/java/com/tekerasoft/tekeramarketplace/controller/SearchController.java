package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.service.SearchItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/search")
public class SearchController {

    private final SearchItemService searchItemService;

    public SearchController(SearchItemService searchItemService) {
        this.searchItemService = searchItemService;
    }

    @GetMapping("/searchProductOrCompany/{input}")
    public ResponseEntity<List<SearchItem>> searchProductOrCompanyQuery(@PathVariable String input) {
        return ResponseEntity.ok(searchItemService.searchProductOrCompany(input));
    }

}
