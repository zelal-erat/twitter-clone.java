package com.twitter.api.controller;

import com.twitter.api.dto.TweetRequest;
import com.twitter.api.dto.TweetResponse;
import com.twitter.api.service.TweetService;
import com.twitter.api.exception.AppException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
@Validated
public class TweetController {

    private final TweetService tweetService;

    @PostMapping
    public ResponseEntity<TweetResponse> createTweet(
            @Valid @RequestBody TweetRequest request,
            Authentication authentication) {

        String username = authentication.getName(); // Giriş yapan kullanıcının username'i
        TweetResponse response = tweetService.createTweet(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetResponse>> findByUserId(
            @NotNull(message = "User ID is required") @RequestParam Long userId) {
        List<TweetResponse> tweets = tweetService.findByUserId(userId);
        return ResponseEntity.ok(tweets);
    }

    @GetMapping("/findById")
    public ResponseEntity<TweetResponse> findById(
            @NotNull(message = "Tweet ID is required") @RequestParam Long id) {
        TweetResponse tweet = tweetService.findById(id);
        return ResponseEntity.ok(tweet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponse> updateTweet(
            @NotNull(message = "Tweet ID is required") @PathVariable Long id,
            @Valid @RequestBody TweetRequest request,
            Authentication authentication) {
        TweetResponse updatedTweet = tweetService.updateTweet(id, request, authentication.getName());
        return ResponseEntity.ok(updatedTweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(
            @NotNull(message = "Tweet ID is required") @PathVariable Long id,
            Authentication authentication) {
        tweetService.deleteTweet(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
