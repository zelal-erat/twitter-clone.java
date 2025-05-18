package com.twitter.api.service;

import com.twitter.api.dto.CommentRequestDTO;
import com.twitter.api.dto.CommentResponseDTO;
import com.twitter.api.entity.Comment;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.CommentRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO, String username) {
        User currentUser = getUserByUsername(username);

        if (requestDTO.getTweetId() == null) {
            throw new AppException("Tweet ID gereklidir.", HttpStatus.BAD_REQUEST);
        }

        Tweet tweet = tweetRepository.findById(requestDTO.getTweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı.", HttpStatus.NOT_FOUND));

        Comment comment = new Comment();
        comment.setContent(requestDTO.getContent());
        comment.setUser(currentUser);
        comment.setTweet(tweet);

        Comment savedComment = commentRepository.save(comment);
        return mapToResponseDTO(savedComment);
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO requestDTO, String username) {
        User currentUser = getUserByUsername(username);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException("Yorum bulunamadı.", HttpStatus.NOT_FOUND));

        if (!comment.getUser().getUsername().equals(currentUser.getUsername())) {
            throw new AppException("Yorumu sadece sahibi güncelleyebilir.", HttpStatus.FORBIDDEN);
        }

        comment.setContent(requestDTO.getContent());
        Comment updated = commentRepository.save(comment);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteComment(Long id, String username) {
        User currentUser = getUserByUsername(username);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException("Yorum bulunamadı.", HttpStatus.NOT_FOUND));

        boolean isOwner = comment.getUser().getUsername().equals(currentUser.getUsername());
        boolean isTweetOwner = comment.getTweet().getUser().getUsername().equals(currentUser.getUsername());

        if (!isOwner && !isTweetOwner) {
            throw new AppException("Yorumu sadece sahibi veya tweet sahibi silebilir.", HttpStatus.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı.", HttpStatus.UNAUTHORIZED));
    }

    private CommentResponseDTO mapToResponseDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser().getUsername());
        dto.setTweetId(comment.getTweet().getId());
        return dto;
    }
}
