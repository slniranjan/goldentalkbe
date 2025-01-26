package com.goldentalk.gt.config.security.service;

import com.goldentalk.gt.config.security.entity.BlackListToken;
import com.goldentalk.gt.config.security.repository.BlackListTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlackListTokenService {

    private final BlackListTokenRepository blackListTokenRepository;

    public void saveToken(BlackListToken token) {
        blackListTokenRepository.save(token);
    }

    @Scheduled(fixedRate = 3600000)
    public void deleteToken() {
        blackListTokenRepository.deleteAll();
    }

    public BlackListToken retriveBlackListToken(String token) {
        return blackListTokenRepository.findByToken(token);
    }

}
