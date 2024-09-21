package com.goldentalk.gt.entity;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Teacher extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  private String name;

  @Column(unique = true)
  private String nic;
  
  @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL,  orphanRemoval = true)
  private Set<Qualification> qualifications = new HashSet<Qualification>();

  @Column(unique = true)
  private String phoneNumber;
  
  @ManyToOne
  private Section section;
  
  @OneToMany(mappedBy = "teacher")
  private Set<Course> courses;
  
  private boolean isDeleted;

}
