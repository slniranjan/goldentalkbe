package com.goldentalk.gt.config.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @NotNull @NotBlank
    @Column(unique = true)
    private String username;

//    @Column(unique = true)
//    private String email;

    @NotNull @NotBlank
    private String password;

    private String roles;

    private boolean firstLogin;

}