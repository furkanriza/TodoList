package com.furkan.repository;

import com.furkan.models.ApplicationUser;
import com.furkan.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer>  {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(ApplicationUser user);
}
