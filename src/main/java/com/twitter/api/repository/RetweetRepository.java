package com.twitter.api.repository;

import com.twitter.api.entity.Retweet;
import com.twitter.api.entity.User;
import com.twitter.api.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    Optional<Retweet> findByUserAndTweet(User user, Tweet tweet);
    void deleteByUserAndTweet(User user, Tweet tweet);
}
