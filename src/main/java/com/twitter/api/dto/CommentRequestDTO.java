package com.twitter.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDTO {
    @NotBlank(message = "Yorum içeriği boş olamaz.")
    private String content;

    @NotNull(message = "Kullanıcı ID boş olamaz.")
    private Long userId;

    @NotNull(message = "Tweet ID boş olamaz.")
    private Long tweetId;

}
