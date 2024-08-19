package com.goldentalk.gt.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Teacher extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer teacherId;
  
  private String name;
  
  private String nic;
  
  @OneToMany(mappedBy = "teacher")
  private Set<Qualification> qualifications;
  
  private String phoneNumber;
  
  @ManyToOne
  private Section section;
  
}
