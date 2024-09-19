package com.goldentalk.gt.entity;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Getter @Setter
public class Course extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq_gen")
//  @SequenceGenerator(name = "course_seq_gen", sequenceName = "course_id_seq", allocationSize = 1)
  private Integer id;

/*  @NotNull
  @Column(unique = true)
  private String courseId;*/

  @NotNull
  private String category;

  @NotNull
  private String name;

  @NotNull
  private boolean isInstallment;

  @Min(1000)
  private double fee;
  
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
  
  /*@PrePersist
  private void generateStudentId() {
    this.courseId = String.format("CRS%05d", this.id);
  }*/
}
