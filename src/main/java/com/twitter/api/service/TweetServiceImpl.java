package com.twitter.api.service;

import com.twitter.api.dto.TweetRequest;
import com.twitter.api.dto.TweetResponse;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public TweetResponse createTweet(TweetRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı: " + username, HttpStatus.NOT_FOUND));

        Tweet tweet = new Tweet();
        tweet.setContent(request.getContent());
        tweet.setUser(user);

        Tweet savedTweet = tweetRepository.save(tweet);

        // 3. Tweet entity'sini TweetResponse DTO'suna dönüştürüp geri döner
        return convertToResponse(savedTweet);
    }

    @Override
    public List<TweetResponse> findByUserId(Long userId) {
        List<Tweet> tweets = tweetRepository.findByUserId(userId);
        if (tweets.isEmpty()) {
            throw new AppException("Tweet bulunamadı", HttpStatus.NOT_FOUND);
        }
        return tweets.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TweetResponse findById(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new AppException("Tweet bulunamadı: " + id, HttpStatus.NOT_FOUND));
        return convertToResponse(tweet);
    }

    @Override
    public TweetResponse updateTweet(Long id, TweetRequest request, String username) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new AppException("Tweet bulunamadı: " + id, HttpStatus.NOT_FOUND));

        if (!tweet.getUser().getUsername().equals(username)) {
            throw new AppException("Bu tweet'i güncelleme yetkiniz yok", HttpStatus.FORBIDDEN);
        }

        tweet.setContent(request.getContent());
        Tweet updatedTweet = tweetRepository.save(tweet);

        return convertToResponse(updatedTweet);
    }

    @Override
    public void deleteTweet(Long id, String username) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new AppException("Tweet bulunamadı: " + id, HttpStatus.NOT_FOUND));

        if (!tweet.getUser().getUsername().equals(username)) {
            throw new AppException("Bu tweet'i silme yetkiniz yok", HttpStatus.FORBIDDEN);
        }

        tweetRepository.delete(tweet);
    }

    private TweetResponse convertToResponse(Tweet tweet) {
        return TweetResponse.builder()
                .id(tweet.getId())
                .content(tweet.getContent())
                .userId(tweet.getUser().getId())
                .username(tweet.getUser().getUsername())
                .createdAt(tweet.getCreatedAt())
                .likesCount(tweet.getLikes() != null ? tweet.getLikes().size() : 0)
                .commentsCount(tweet.getComments() != null ? tweet.getComments().size() : 0)
                .retweetsCount(tweet.getRetweets() != null ? tweet.getRetweets().size() : 0)
                .build();
    }
}