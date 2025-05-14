package com.twitter.api.service;

import com.twitter.api.dto.LikeRequest;
import com.twitter.api.dto.LikeResponse;
import com.twitter.api.entity.Like;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.LikeRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import com.twitter.api.service.LikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public LikeResponse likeTweet(LikeRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));
        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı", HttpStatus.NOT_FOUND));


        if (likeRepository.findByUserAndTweet(user, tweet).isPresent()) {
            throw new AppException("Zaten beğenmişsiniz.", HttpStatus.BAD_REQUEST);
        }

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        Like saved = likeRepository.save(like);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void dislikeTweet(LikeRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));
        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı", HttpStatus.NOT_FOUND));

        Like like = likeRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new AppException("Beğeni bulunamadı", HttpStatus.NOT_FOUND));

        likeRepository.delete(like);
    }

    private LikeResponse toResponse(Like like) {
        return new LikeResponse(like.getId(), like.getUser().getId(), like.getTweet().getId());
    }
}
