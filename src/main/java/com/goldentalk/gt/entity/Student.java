package com.goldentalk.gt.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

//@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq_gen")
  @SequenceGenerator(name = "student_seq_gen", sequenceName = "student_id_seq", allocationSize = 1)
  private Integer id;
  
  private String studentId;
  
  private String firstName;
  
  private String middleName;
  
  private String lastName;
  
  @Column(unique = true)
  private String whatsappNum;

  @Column(unique = true)
  private String nic;

  @Column(unique = true)
  private String email;
  
  @ManyToOne(cascade = CascadeType.ALL)
  private Address address;
  
  @ManyToMany
  @JoinTable(
      name = "student_section",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "section_id")
  ) 
  private Set<Section> sections;
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Payment> payments;
  
  @ManyToMany
  @JoinTable(
      name = "student_course",
      joinColumns  = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id")      
  )
  private Set<Course> courses;
  
  private boolean deleted;

  @PrePersist
  private void generateStudentId() {

    String sectionName = sections.stream().findAny().orElseThrow().getSectionName();
    this.studentId = String.format(sectionName + LocalDate.now().getYear() % 100 +"%04d", this.id);
  }
  
}
