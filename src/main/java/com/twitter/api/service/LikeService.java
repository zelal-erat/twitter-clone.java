package com.twitter.api.service;


import com.twitter.api.dto.LikeRequest;
import com.twitter.api.dto.LikeResponse;

public interface LikeService {
    LikeResponse likeTweet(LikeRequest request);
    void dislikeTweet(LikeRequest request);
}
