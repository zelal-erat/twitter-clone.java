package com.twitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private String username;
    private Long tweetId;


}
