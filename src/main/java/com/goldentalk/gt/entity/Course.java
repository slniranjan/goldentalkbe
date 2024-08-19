package com.goldentalk.gt.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Course extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String category;
  
  private String name;
  
  private boolean isInstallment;
  
  private int installmentCount;
  
  private double amount;
  
  @ManyToOne
  private Section section;
  
  @ManyToOne
  private Teacher teacher;
  
  @ManyToMany(mappedBy = "courses")
  private Set<Student> students;
  
  private boolean isDeleted;
}
