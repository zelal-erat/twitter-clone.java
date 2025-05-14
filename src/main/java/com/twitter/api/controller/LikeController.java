package com.twitter.api.controller;


import com.twitter.api.dto.LikeRequest;
import com.twitter.api.dto.LikeResponse;
import com.twitter.api.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<LikeResponse> likeTweet(@Valid @RequestBody LikeRequest request) {
        return ResponseEntity.ok(likeService.likeTweet(request));
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeTweet(@Valid @RequestBody LikeRequest request) {
        likeService.dislikeTweet(request);
        return ResponseEntity.ok().build();
    }
}
