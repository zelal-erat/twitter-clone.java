package com.twitter.api.dto;

public record LikeResponse(Long likeId, Long userId, Long tweetId, String tweetContent) {
}
