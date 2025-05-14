package com.twitter.api.mapper;

import com.twitter.api.dto.TweetResponse;
import com.twitter.api.entity.Tweet;
import org.springframework.stereotype.Component;

@Component
public class ResponseConverter {
    public TweetResponse convertToTweetResponse(Tweet tweet) {
        return TweetResponse.builder()
                .id(tweet.getId())
                .content(tweet.getContent())
                .userId(tweet.getUser().getId())
                .username(tweet.getUser().getUsername())
                .createdAt(tweet.getCreatedAt())
                .build();
    }
}
