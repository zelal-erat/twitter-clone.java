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
import com.twitter.api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı.", HttpStatus.NOT_FOUND));

        Tweet tweet = tweetRepository.findById(requestDTO.getTweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı.", HttpStatus.NOT_FOUND));

        Comment comment = new Comment();
        comment.setContent(requestDTO.getContent());
        comment.setUser(user);
        comment.setTweet(tweet);

        Comment savedComment = commentRepository.save(comment);

        return mapToResponseDTO(savedComment);
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException("Yorum bulunamadı.", HttpStatus.NOT_FOUND));

        if (!comment.getUser().getId().equals(requestDTO.getUserId())) {
            throw new AppException("Yorumu sadece sahibi güncelleyebilir.", HttpStatus.UNAUTHORIZED);
        }

        comment.setContent(requestDTO.getContent());
        Comment updated = commentRepository.save(comment);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteComment(Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException("Yorum bulunamadı.", HttpStatus.NOT_FOUND));

        boolean isOwner = comment.getUser().getId().equals(userId);
        boolean isTweetOwner = comment.getTweet().getUser().getId().equals(userId);

        if (!isOwner && !isTweetOwner) {
            throw new AppException("Yorumu sadece sahibi veya tweet sahibi silebilir.", HttpStatus.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
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
