package com.twitter.api.repository;

import com.twitter.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //  kullanıcıyı bulmak için
    @Query("select u from User u where u.username= :username")
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);



}
