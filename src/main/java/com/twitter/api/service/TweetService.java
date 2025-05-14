package com.twitter.api.service;

import com.twitter.api.dto.TweetRequest;
import com.twitter.api.dto.TweetResponse;

import java.util.List;

public interface TweetService {

    TweetResponse createTweet(TweetRequest request, String username);

    List<TweetResponse> findByUserId(Long userId);

    TweetResponse findById(Long id);

    TweetResponse updateTweet(Long id, TweetRequest request, String username);

    void deleteTweet(Long id, String username);
}
