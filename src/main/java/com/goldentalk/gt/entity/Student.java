package com.goldentalk.gt.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Entity
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@Data
@Builder
public class Student extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq_gen")
  @SequenceGenerator(name = "student_seq_gen", sequenceName = "student_id_seq", allocationSize = 1)
  private Integer id;
  
  private String studentId;
  
  private String firstName;
  
  private String middleName;
  
  private String lastName;
  
//  private String dob;
  @Column(unique = true)
  private String whatsappNum;
  
  @ManyToOne(cascade = CascadeType.ALL)
  private Address address;
  
  @ManyToMany
  @JoinTable(
      name = "student_section",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "section_id")
  ) 
  private Set<Section> sections;
  
  @OneToMany(mappedBy = "student")
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

    String sectionName = sections.stream().findAny().get().getSectionName();
    this.studentId = String.format(sectionName +"%05d", this.id);
  }
  
}
