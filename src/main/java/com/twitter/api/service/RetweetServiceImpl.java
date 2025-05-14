package com.twitter.api.service;

import com.twitter.api.dto.RetweetRequest;
import com.twitter.api.dto.RetweetResponse;
import com.twitter.api.entity.Retweet;
import com.twitter.api.entity.User;
import com.twitter.api.entity.Tweet;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.RetweetRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public RetweetResponse retweet(RetweetRequest request) {
        User user = getUserById(request.getUserId());
        Tweet tweet = getTweetById(request.getTweetId());

        // Zaten retweet yapılmış mı?
        if (retweetRepository.findByUserAndTweet(user, tweet).isPresent()) {
            throw new AppException("Tweet zaten retweet edilmiş", HttpStatus.BAD_REQUEST);
        }

        // Yeni retweet oluştur
        Retweet retweet = new Retweet();
        Retweet savedRetweet = retweetRepository.save(retweet);

        return toResponse(savedRetweet);
    }

    @Override
    @Transactional
    public void deleteRetweet(Long userId, Long tweetId) {
        User user = getUserById(userId);
        Tweet tweet = getTweetById(tweetId);

        // Retweet var mı?
        Retweet retweet = retweetRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new AppException("Retweet bulunamadı", HttpStatus.NOT_FOUND));

        retweetRepository.delete(retweet);
    }

    // DTO dönüşüm metodu
    private RetweetResponse toResponse(Retweet retweet) {
        return new RetweetResponse(retweet.getId(), retweet.getUser().getId(), retweet.getTweet().getId());
    }

    // Kullanıcıyı id ile al
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));
    }

    // Tweeti id ile al
    private Tweet getTweetById(Long tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new AppException("Tweet bulunamadı", HttpStatus.NOT_FOUND));
    }
}
