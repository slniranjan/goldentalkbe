package com.goldentalk.gt.config.security.service;


import com.goldentalk.gt.config.security.entity.UserInfo;
import com.goldentalk.gt.config.security.repository.UserInfoRepository;
import com.goldentalk.gt.exception.AlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {

        try {
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));

            repository.save(userInfo);
            return "User Added Successfully";
        } catch (DataIntegrityViolationException dive) {
            throw new AlreadyExistsException(dive.getMessage());
        }

    }

    public UserInfo retrieveUserInfo(String email) {
        return repository.findByEmail(email).get();
    }
}