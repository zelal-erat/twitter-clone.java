package com.twitter.api.repository;

import com.twitter.api.entity.Like;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndTweet(User user, Tweet tweet);
}
