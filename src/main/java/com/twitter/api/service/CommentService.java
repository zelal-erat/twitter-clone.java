package com.twitter.api.service;

import com.twitter.api.dto.CommentRequestDTO;
import com.twitter.api.dto.CommentResponseDTO;

public interface CommentService {
    CommentResponseDTO createComment(CommentRequestDTO requestDTO, String username);
    CommentResponseDTO updateComment(Long id, CommentRequestDTO requestDTO, String username);
    void deleteComment(Long id, String username);
}
