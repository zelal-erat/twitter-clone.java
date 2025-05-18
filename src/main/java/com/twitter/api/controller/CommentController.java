package com.twitter.api.controller;

import com.twitter.api.dto.CommentRequestDTO;
import com.twitter.api.dto.CommentResponseDTO;
import com.twitter.api.dto.MessageResponse;
import com.twitter.api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> create(@RequestBody @Valid CommentRequestDTO requestDTO,
                                                     Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDTO response = commentService.createComment(requestDTO, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable Long id,
                                                     @RequestBody @Valid CommentRequestDTO requestDTO,
                                                     Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDTO response = commentService.updateComment(id, requestDTO, username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        commentService.deleteComment(id, username);
        return ResponseEntity.ok(new MessageResponse("Yorum başarıyla silindi."));
    }
}
