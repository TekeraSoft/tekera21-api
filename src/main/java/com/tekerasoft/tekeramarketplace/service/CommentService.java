package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CommentUiDto;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.exception.UnauthorizedException;
import com.tekerasoft.tekeramarketplace.model.entity.Comment;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CommentRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthenticationFacade authenticationFacade;
    private final FileService fileService;

    public CommentService(CommentRepository commentRepository, AuthenticationFacade authenticationFacade, FileService fileService) {
        this.commentRepository = commentRepository;
        this.authenticationFacade = authenticationFacade;
        this.fileService = fileService;
    }

    public Page<CommentUiDto> getProductComments(String productId, Pageable pageable) {
        return commentRepository.findByProductIdOrderByCreatedAtDesc(UUID.fromString(productId), pageable)
                .map(CommentUiDto::toDto);
    }

    public ApiResponse<?> deleteComment(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new NotFoundException("Yorum bulunamadı"));
        if(comment.getUser().getId().toString().equals(authenticationFacade.getCurrentUserId())) {
            for(String url: comment.getProductImages()) {
                fileService.deleteInFolderFile(url);
            }
            commentRepository.delete(comment);
            return new ApiResponse<>("Yorum kaldırıldı", HttpStatus.OK.value());
        } else {
            throw new UnauthorizedException("Yetkisiz işlem");
        }
    }
}
