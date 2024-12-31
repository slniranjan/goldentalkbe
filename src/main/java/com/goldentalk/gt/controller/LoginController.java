package com.goldentalk.gt.controller;

import com.goldentalk.gt.dto.JwtResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class LoginController {
    @Autowired
    JwtEncoder encoder;

    @PostMapping("/token")
    public JwtResponse token(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed!");
        }
        System.out.println("Authenticated user: " + authentication.getName());

        Instant now = Instant.now();
        long expiry = 36000L;

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return JwtResponse.builder()
                .jwt(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .build();
    }
}
