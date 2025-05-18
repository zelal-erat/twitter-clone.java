package com.twitter.api.service;

import com.twitter.api.dto.RetweetRequest;
import com.twitter.api.dto.RetweetResponse;

public interface RetweetService {

    RetweetResponse retweet(RetweetRequest request, String username);

    void deleteRetweet(Long retweetId, String username);
}
