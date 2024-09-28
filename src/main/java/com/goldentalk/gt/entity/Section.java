package com.goldentalk.gt.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "section_name", unique=true)
  private String sectionName;
  
  @ManyToMany(mappedBy = "sections", fetch = FetchType.EAGER)
  private Set<Student> students;
  
  @OneToMany(mappedBy = "section")
  private Set<Teacher> teacher;
  
  @OneToMany(mappedBy = "section" )
  private Set<Course> courses;

  private boolean deleted;

}
