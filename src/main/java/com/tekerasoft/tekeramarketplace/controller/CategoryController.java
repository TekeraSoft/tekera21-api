package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}
