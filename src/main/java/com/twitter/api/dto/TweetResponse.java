package com.twitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponse {
    private Long id;
    private String content;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private int likesCount;
    private int commentsCount;
    private int retweetsCount;
} 