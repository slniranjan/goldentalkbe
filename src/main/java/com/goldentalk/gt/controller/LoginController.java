package com.goldentalk.gt.controller;


import com.goldentalk.gt.config.security.entity.*;
import com.goldentalk.gt.config.security.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final UserInfoService userInfoService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final BlackListTokenService blackListTokenService;

    private final PasswordEncoder encoder;


    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {

        return userInfoService.addUser(userInfo);

    }

    @PostMapping("/login")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {

            UserInfoDetails userDetials = (UserInfoDetails) authentication.getPrincipal();

            if(userDetials.isFirstLogin()) {
                return JwtResponse.builder()
                        .accessToken(jwtService.generateToken(authRequest.getUsername()))
                        .redirectUrl("/gt/auth/change-password").build();
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .token(refreshToken.getToken()).build();

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest request) {

        UserInfoDetails userDetails = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserInfo userInfo = userInfoService.retrieveUserInfo(userDetails.getUsername());

        userInfo.setPassword(encoder.encode(request.getNewPassword()));
        userInfo.setRoles(userInfo.getRoles().replace("_NEW", ""));
        userInfo.setFirstLogin(false);

        userInfoService.updateUserInfo(userInfo);
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authHeader) {

        BlackListToken blackListToken = new BlackListToken();
        blackListToken.setToken(authHeader.substring(7));

        blackListTokenService.saveToken(blackListToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null && !authentication.isAuthenticated()) {
            return;
        }
        UserInfo userInfo = userInfoService.retrieveUserInfo(authentication.getName());
        RefreshToken refreshToken = refreshTokenService.retrieveRefreshToken(userInfo);

        refreshTokenService.deleteRefreshToken(refreshToken);


    }
}
