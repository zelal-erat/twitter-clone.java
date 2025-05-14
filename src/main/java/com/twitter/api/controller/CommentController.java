package com.twitter.api.controller;


import com.twitter.api.dto.CommentRequestDTO;
import com.twitter.api.dto.CommentResponseDTO;
import com.twitter.api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> create(@RequestBody @Valid CommentRequestDTO requestDTO) {
        return new ResponseEntity<>(commentService.createComment(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.updateComment(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ResponseEntity.noContent().build();
    }
}
