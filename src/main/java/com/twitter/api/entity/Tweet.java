package com.twitter.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tweet", schema = "twitter")
public class Tweet {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "tweet_id")
     private Long id;

     @Column(name = "content")
     @NotBlank(message = "Tweet content cannot be empty")
     @Size(min = 1, max = 280, message = "Tweet must be between 1 and 280 characters")
     private String content;

     @Column(name = "created_at")
     private LocalDateTime createdAt;

     @PrePersist
     protected void onCreate() {
         createdAt = LocalDateTime.now();
     }

     @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
     @JoinColumn(name = "user_id")
     private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private List<Like> likes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private List<Retweet> retweets;


}
