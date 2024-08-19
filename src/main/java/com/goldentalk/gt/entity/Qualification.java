package com.goldentalk.gt.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Qualification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  
  private String qualification;
  
  private String institute;
  
  @ManyToOne
  private Teacher teacher;
  
}
