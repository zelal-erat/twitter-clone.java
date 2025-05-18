package com.twitter.api.service;

import com.twitter.api.dto.RetweetRequest;
import com.twitter.api.dto.RetweetResponse;
import com.twitter.api.entity.Retweet;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.RetweetRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import com.twitter.api.service.RetweetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public RetweetResponse retweet(RetweetRequest request, String username) {
        // Kullanıcıyı username ile bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı.", HttpStatus.UNAUTHORIZED));

        // Tweet'i bul
        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new AppException("Tweet bulunamadı", HttpStatus.NOT_FOUND));

        // Zaten retweet etmiş mi kontrol et
        if (retweetRepository.findByUserAndTweet(user, tweet).isPresent()) {
            throw new AppException("Tweet zaten retweet edilmiş", HttpStatus.BAD_REQUEST);
        }

        // Yeni retweet oluştur
        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);

        Retweet savedRetweet = retweetRepository.save(retweet);

        return toResponse(savedRetweet);
    }

    @Override
    @Transactional
    public void deleteRetweet(Long retweetId, String username) {
        // Kullanıcıyı bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı.", HttpStatus.UNAUTHORIZED));

        // Retweet'i bul
        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new AppException("Retweet bulunamadı", HttpStatus.NOT_FOUND));

        // Sahibinin aynı kullanıcı olup olmadığını kontrol et
        if (!retweet.getUser().getUsername().equals(user.getUsername())) {
            throw new AppException("Bu retweet size ait değil", HttpStatus.FORBIDDEN);
        }

        retweetRepository.delete(retweet);
    }

    private RetweetResponse toResponse(Retweet retweet) {
        return new RetweetResponse(
                retweet.getId(),
                retweet.getUser().getId(),
                retweet.getTweet().getId()
        );
    }
}
