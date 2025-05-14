package com.twitter.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



public record LikeRequest(
        @NotNull
        Long userId,
        @NotNull
        Long tweetId

) {}