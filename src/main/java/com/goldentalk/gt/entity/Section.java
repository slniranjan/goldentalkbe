package com.goldentalk.gt.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Section extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String sectionName;
  
  @ManyToMany(mappedBy = "sections")
  private Set<Student> students;
  
  @OneToMany(mappedBy = "section")
  private Set<Teacher> teacher;
  
  @OneToMany(mappedBy = "section" )
  private Set<Course> courses;
  
  private boolean deleted;
 
}
