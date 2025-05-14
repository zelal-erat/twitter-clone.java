package com.twitter.api.service;

import com.twitter.api.dto.RetweetRequest;
import com.twitter.api.dto.RetweetResponse;

public interface RetweetService {

    /**
     * Kullanıcının bir tweeti retweet etmesini sağlar.
     * Aynı tweeti tekrar retweet edemez.
     */
    RetweetResponse retweet(RetweetRequest request);

    /**
     * Kullanıcının retweetini silmesini sağlar.
     * Sadece ilgili kullanıcı kendi retweetini silebilir.
     */
    void deleteRetweet(Long userId, Long tweetId);
}
