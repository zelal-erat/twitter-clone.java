package com.twitter.api.controller;

import com.twitter.api.dto.RetweetRequest;
import com.twitter.api.dto.RetweetResponse;
import com.twitter.api.service.RetweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetweetResponse retweet(@RequestBody RetweetRequest request) {
        return retweetService.retweet(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetweet(@PathVariable Long id, @RequestParam Long tweetId) {
        retweetService.deleteRetweet(id, tweetId);
    }
}
