package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.EsSearchRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.esdocument.Item;
import com.tekerasoft.tekeramarketplace.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item createIndex(@RequestBody  Item item) {
        return itemService.createIndex(item);
    }

    @DeleteMapping
    public ApiResponse<?> deleteIndex(@RequestParam String id) {
        return  itemService.deleteIndex(id);
    }

    @GetMapping("/addItemsFromJson")
    public void addItemsFromJson(){
        itemService.addItemsFromJson();
    }

    @GetMapping("/getAllDataFromIndex/{indexName}")
    public List<Item> getAllDataFromIndex(@PathVariable String indexName){
        return itemService.getAllDataFromIndex(indexName);
    }

    @GetMapping("/search")
    public List<Item> searchItemsByFieldAndValue(@RequestBody EsSearchRequest req){
        return itemService.searchItemsByFieldAndValue(req);
    }

    @GetMapping("/search/{name}/{brand}")
    public List<Item> searchItemsByNameAndBrandWithQuery(@PathVariable String name, @PathVariable String brand){
        return itemService.searchItemsByNameAndBrandWithQuery(name,brand);
    }

    @GetMapping("/boolQuery")
    public List<Item> boolQuery(@RequestBody EsSearchRequest req) {
        return itemService.boolQuery(req);
    }

    @GetMapping("/autoSuggest")
    public Set<String> autoSuggestItemsByName(@RequestParam String name) {
        return itemService.findSuggestedItemNames(name);
    }

    @GetMapping("/suggestionsQuery")
    public List<String> autoSuggestItemsByNameWithQuery(@RequestParam String name) {
        return itemService.autoSuggestItemsByNameWithQuery(name);
    }

}
