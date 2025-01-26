package com.goldentalk.gt.config.security.repository;

import com.goldentalk.gt.config.security.entity.BlackListToken;
import com.goldentalk.gt.config.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Integer> {

    BlackListToken findByToken(String token);

}
