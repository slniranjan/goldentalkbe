package com.goldentalk.gt.entity;

import java.util.Date;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Student extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq_gen")
  @SequenceGenerator(name = "student_seq_gen", sequenceName = "student_id_seq", allocationSize = 1)
  private Integer id;
  
  private String studentId;
  
  private String ineternalId;
  
  private String fisrtName;
  
  private String lastName;
  
  private Date dob;
  
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

  @PrePersist
  private void generateStudentId() {
    this.studentId = String.format("stu%05d", this.id);
  }
  
}
