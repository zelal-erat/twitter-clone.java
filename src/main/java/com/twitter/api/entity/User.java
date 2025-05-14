package com.twitter.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user", schema = "twitter")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores and hyphens")
    private String username;

    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Like> likes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Retweet> retweets;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Tweet> tweets;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
