package com.twitter.api.service;

import com.twitter.api.dto.LoginRequest;
import com.twitter.api.dto.LoginResponse;
import com.twitter.api.dto.RegisterRequest;
import com.twitter.api.dto.RegisterResponse;
import com.twitter.api.entity.User;
import com.twitter.api.exception.AppException;
import com.twitter.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists.", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return new RegisterResponse("Kayıt başarılı!", savedUser.getId());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Şifre yanlış", HttpStatus.UNAUTHORIZED);
        }

        return new LoginResponse("Giriş başarılı!", user.getUsername());
    }
}
