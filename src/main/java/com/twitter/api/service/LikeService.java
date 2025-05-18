package com.twitter.api.service;

import com.twitter.api.dto.LikeRequest;
import com.twitter.api.dto.LikeResponse;
import com.twitter.api.dto.MessageResponse;

public interface LikeService {
    LikeResponse likeTweet(LikeRequest request, String username);
    MessageResponse dislikeTweet(LikeRequest request, String username);
}
