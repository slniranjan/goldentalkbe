package com.goldentalk.gt.entity;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Teacher extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @SequenceGenerator(name = "teacher_seq_gen", sequenceName = "teacher_id_seq", allocationSize = 1)
  private Integer id;
  
  private String name;
  
  private String nic;
  
  @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL,  orphanRemoval = true)
  private Set<Qualification> qualifications = new HashSet<Qualification>();
  
  private String phoneNumber;
  
  @ManyToOne
  private Section section;
  
  @OneToMany(mappedBy = "teacher")
  private Set<Course> courses;
  
  private boolean isDeleted;
  
  /*@PrePersist
  private void generateStudentId() {
    this.teacherId = String.format("TEA%05d", this.id);
  }*/
  
}
