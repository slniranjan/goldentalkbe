package com.goldentalk.gt.config.security.service;

import com.goldentalk.gt.config.security.entity.RefreshToken;
import com.goldentalk.gt.config.security.repository.RefreshTokenRepository;
import com.goldentalk.gt.config.security.repository.UserInfoRepository;
import com.goldentalk.gt.exception.AlreadyLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public RefreshToken createRefreshToken(String username) {
        try {
            RefreshToken refreshToken = RefreshToken.builder()
                    .userInfo(userInfoRepository.findByEmail(username).get())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(120000))//10
                    .build();
            return refreshTokenRepository.save(refreshToken);
        } catch(DataIntegrityViolationException die) {
            if (die.getMessage().indexOf("refresh_token") != -1) {
                throw new AlreadyLoginException("You are already logged in");
            }
            throw new RuntimeException(die.getMessage());
        }

    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

}
