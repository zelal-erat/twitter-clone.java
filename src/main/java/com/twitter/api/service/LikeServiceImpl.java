package com.twitter.api.service;

import com.twitter.api.dto.LikeRequest;
import com.twitter.api.dto.LikeResponse;
import com.twitter.api.dto.MessageResponse;
import com.twitter.api.entity.Like;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.LikeRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public LikeResponse likeTweet(LikeRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı.", HttpStatus.UNAUTHORIZED));

        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı.", HttpStatus.NOT_FOUND));

        if (likeRepository.findByUserAndTweet(user, tweet).isPresent()) {
            throw new AppException("Zaten beğenmişsiniz.", HttpStatus.BAD_REQUEST);
        }

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);

        Like saved = likeRepository.save(like);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public MessageResponse dislikeTweet(LikeRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı.", HttpStatus.UNAUTHORIZED));

        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı.", HttpStatus.NOT_FOUND));

        Like like = likeRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new AppException("Beğeni bulunamadı.", HttpStatus.NOT_FOUND));

        likeRepository.delete(like);
        return new MessageResponse("Beğeni başarıyla kaldırıldı.");
    }

    private LikeResponse convertToResponse(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getUser().getId(),
                like.getTweet().getId(),
                like.getTweet().getContent()
        );
    }

}
