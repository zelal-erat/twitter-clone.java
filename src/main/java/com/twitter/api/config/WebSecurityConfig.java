package com.twitter.api.config;

import com.twitter.api.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
                .csrf(csrf -> csrf.disable())
               // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login").permitAll()
                        .anyRequest().authenticated()
                )

               .formLogin(login -> login.disable())
                .httpBasic(Customizer.withDefaults())
                .build();


    }

    // @Bean
    //    public CorsConfigurationSource corsConfigurationSource() {
    //        CorsConfiguration configuration = new CorsConfiguration();
    //
    //        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3200")); // React portu
    //        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //        configuration.setAllowedHeaders(Collections.singletonList("*"));
    //        configuration.setAllowCredentials(true); // eğer token veya cookie varsa
    //
    //        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //        source.registerCorsConfiguration("/**", configuration);
    //        return source;
    //    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Veritabanı kullanıcı doğrulama işlemi

        provider.setUserDetailsService(userDetailsService);
        // Kullanıcı bilgilerini veritabanından alacak olan servis atanıyor

        provider.setPasswordEncoder(passwordEncoder());
        // Kullanıcı şifresinin doğrulanması için şifreleme algoritması (BCrypt) atanıyor

        return new ProviderManager(provider);
        // Oluşturulan provider, bir AuthenticationManager olarak döndürülüyor
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
