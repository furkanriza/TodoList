package com.furkan.services;

import com.furkan.models.ApplicationUser;
import com.furkan.models.RefreshToken;
import com.furkan.repository.RefreshTokenRepository;
import com.furkan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(36000))//10
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token has been expired.");
        }
        return token;
    }

    public RefreshToken getOrCreateRefreshToken(String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            RefreshToken token = existingToken.get();
            if (token.getExpiryDate().isAfter(Instant.now())) {
                // If the existing token is still valid, return it.
                return token;
            } else {
                // If the existing token has expired, delete it from the database.
                refreshTokenRepository.delete(token);
            }
        }

        // Create a new refresh token since no valid one exists.
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(36000)) // 10 hours in the future
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}