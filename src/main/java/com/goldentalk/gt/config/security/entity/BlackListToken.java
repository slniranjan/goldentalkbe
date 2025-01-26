package com.goldentalk.gt.config.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.annotation.processing.Generated;

@Getter
@Setter
@Entity(name = "black_list_token")
public class BlackListToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;
}
