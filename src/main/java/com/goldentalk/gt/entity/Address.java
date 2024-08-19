package com.goldentalk.gt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  
  private String street;
  
  private String city;
  
  private String district;
  
  private String province;
  
}
