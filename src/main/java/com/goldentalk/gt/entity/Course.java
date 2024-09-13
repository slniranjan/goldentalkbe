package com.goldentalk.gt.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Course extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq_gen")
  @SequenceGenerator(name = "course_seq_gen", sequenceName = "course_id_seq", allocationSize = 1)
  private Integer id;
  
  private String courseId;
  
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
  
//  @OneToMany(mappedBy = "course")
//  private Set<Payment> payments;
  
//  @OneToOne
//  private Payment payment;
  
  private boolean isDeleted;
  
  @PrePersist
  private void generateStudentId() {
    this.courseId = String.format("COURSE%05d", this.id);
  }
}
