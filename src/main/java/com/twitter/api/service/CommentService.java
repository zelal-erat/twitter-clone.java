package com.twitter.api.service;

import com.twitter.api.dto.CommentRequestDTO;
import com.twitter.api.dto.CommentResponseDTO;

public interface CommentService {
    CommentResponseDTO createComment(CommentRequestDTO requestDTO);
    CommentResponseDTO updateComment(Long id, CommentRequestDTO requestDTO);
    void deleteComment(Long id, Long userId);
}
