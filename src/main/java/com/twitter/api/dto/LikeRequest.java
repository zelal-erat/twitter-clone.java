package com.twitter.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LikeRequest {

        @NotNull
        Long tweetId;
}