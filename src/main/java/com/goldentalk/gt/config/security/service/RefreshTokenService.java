package com.goldentalk.gt.config.security.service;

import com.goldentalk.gt.config.security.entity.RefreshToken;
import com.goldentalk.gt.config.security.entity.UserInfo;
import com.goldentalk.gt.config.security.repository.RefreshTokenRepository;
import com.goldentalk.gt.config.security.repository.UserInfoRepository;
import com.goldentalk.gt.exception.AlreadyLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserInfoRepository userInfoRepository;

    @Value("${jwt.refresh.token.expiration.milliseconds}")
    private long refreshTokenExpiration;

    public RefreshToken createRefreshToken(String username) {

        UserInfo userInfo = userInfoRepository.findByUsername(username).get();
        RefreshToken existingRefreshToken = refreshTokenRepository.findByUserInfo(userInfo);

        if(existingRefreshToken != null) {
            refreshTokenRepository.delete(existingRefreshToken);
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);

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

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public RefreshToken retrieveRefreshToken(UserInfo userInfo) {
        return refreshTokenRepository.findByUserInfo(userInfo);
    }
}
