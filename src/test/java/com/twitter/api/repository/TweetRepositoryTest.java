package com.twitter.api.repository;

import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class TweetRepositoryTest {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find tweets by userId")
    void shouldFindTweetsByUserId() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Tweet tweet1 = new Tweet();
        tweet1.setContent("First tweet");
        tweet1.setUser(user);
        tweetRepository.save(tweet1);

        Tweet tweet2 = new Tweet();
        tweet2.setContent("Second tweet");
        tweet2.setUser(user);
        tweetRepository.save(tweet2);

        // Act
        List<Tweet> tweets = tweetRepository.findByUserId(user.getId());

        // Assert
        assertThat(tweets).hasSize(2);
        assertThat(tweets.get(0).getUser().getId()).isEqualTo(user.getId());
    }
}
