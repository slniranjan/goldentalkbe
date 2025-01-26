package com.goldentalk.gt.config.security.repository;

import com.goldentalk.gt.config.security.entity.RefreshToken;
import com.goldentalk.gt.config.security.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken findByUserInfo(UserInfo userInfo);

    void deleteByUserInfo(UserInfo userInfo);

}
