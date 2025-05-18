package com.twitter.api.controller;

import com.twitter.api.dto.RetweetRequest;
import com.twitter.api.dto.RetweetResponse;
import com.twitter.api.service.RetweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetweetResponse retweet(
            @Valid @RequestBody RetweetRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        return retweetService.retweet(request, username);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetweet(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        retweetService.deleteRetweet(id, username);
    }



}
