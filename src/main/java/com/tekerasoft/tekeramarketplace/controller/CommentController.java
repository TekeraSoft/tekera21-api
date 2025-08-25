package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.CommentUiDto;
import com.tekerasoft.tekeramarketplace.service.CommentService;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/getProductComments")
    public ResponseEntity<Page<CommentUiDto>> getProductComments(@RequestParam String productId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getProductComments(productId, pageable));
    }
}
