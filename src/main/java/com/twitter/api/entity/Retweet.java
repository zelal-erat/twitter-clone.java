package com.twitter.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "retweet", schema = "twitter")
public class Retweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retweet_id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @NotNull(message = "Kullanıcı ID'si boş olamaz")
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "tweet_id")
    @NotNull(message = "Tweet ID'si boş olamaz")
    private Tweet tweet;


}
