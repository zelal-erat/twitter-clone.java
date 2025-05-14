package com.twitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetweetResponse {
    private Long id;
    private Long userId;
    private Long tweetId;
}
