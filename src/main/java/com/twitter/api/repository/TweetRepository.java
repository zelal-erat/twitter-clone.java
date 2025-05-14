package com.twitter.api.repository;

import com.twitter.api.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query("SELECT t FROM Tweet t WHERE t.user.id = :userId")
    List<Tweet> findByUserId(@Param("userId") Long userId);

}