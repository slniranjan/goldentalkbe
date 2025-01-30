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
  private Integer id;

  @NotNull
  private String category;

  @NotNull
  private String name;

  @NotNull
  private Boolean installment;

  @Min(1000)
  private double fee;

  private double discount = 0.0;
  
  @ManyToOne
  private Section section;
  
  @ManyToOne
  private Teacher teacher;
  
  @ManyToMany(mappedBy = "courses")
  private Set<Student> students;
  
  private boolean isDeleted;

//  private Boolean activeDiscount = false;

}
