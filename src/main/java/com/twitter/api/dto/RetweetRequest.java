package com.twitter.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetweetRequest {
    @NotNull(message = "Kullanıcı ID'si boş olamaz")
    private Long userId;

    @NotNull(message = "Tweet ID'si boş olamaz")
    private Long tweetId;
}
