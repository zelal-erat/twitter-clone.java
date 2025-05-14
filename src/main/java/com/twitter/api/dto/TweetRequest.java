package com.twitter.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetRequest {
    @NotBlank(message = "Tweet content cannot be empty")
    @Size(min = 1, max = 280, message = "Tweet must be between 1 and 280 characters")
    private String content;
}