package com.twitter.api.controller;

import com.twitter.api.dto.LikeRequest;
import com.twitter.api.dto.LikeResponse;
import com.twitter.api.dto.MessageResponse;
import com.twitter.api.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<LikeResponse> likeTweet(@Valid @RequestBody LikeRequest request,
                                                  Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(likeService.likeTweet(request, username));
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<MessageResponse> dislikeTweet(@Valid @RequestBody LikeRequest request,
                                                        Authentication authentication) {
        String username = authentication.getName();
        MessageResponse response = likeService.dislikeTweet(request, username);
        return ResponseEntity.ok(response);
    }
}
