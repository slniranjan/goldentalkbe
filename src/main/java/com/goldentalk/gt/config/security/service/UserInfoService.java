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
        Optional<UserInfo> userDetail = repository.findByUsername(username);

        // Converting UserInfo to UserDetails
        UserDetails userDetails =  userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return userDetails;
    }

    public String addUser(UserInfo userInfo) {

        try {
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));
            userInfo.setFirstLogin(true);
            userInfo.setRoles(userInfo.getRoles().concat("_NEW"));

            repository.save(userInfo);
            return "User Added Successfully";
        } catch (DataIntegrityViolationException dive) {
            throw new AlreadyExistsException(dive.getMessage().contains("already exists") ? "Username already exists" : dive.getMessage());
        }

    }

    public UserInfo retrieveUserInfo(String username) {
        return repository.findByUsername(username).get();
    }

    public void updateUserInfo(UserInfo userInfo) {
        repository.save(userInfo);
    }
}