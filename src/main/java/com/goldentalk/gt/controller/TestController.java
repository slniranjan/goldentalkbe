package com.goldentalk.gt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Student;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.StudentRepository;

@RestController
public class TestController {

  @Autowired
  SectionRepository sRepo;
  
  @Autowired
  StudentRepository repo;
  
  @GetMapping("/test")
  public void update() {
//    Student stu = new Student();
//    stu.setFisrtName("Harshana");
//    stu.setLastName("Jayamaha");
//    stu.setIneternalId("pte1121");
//    stu.set
//    
//    Address address = new Address();
//    address.setCity("kegalle");
//    address.setProvince("sabaragamuwa");
//    address.setStreet("main street");
//    
//    stu.setAddress(address);
    
    
//    repo.save(stu);
    
    Student  studnet = repo.findById(1).get();
    
    Section section = new Section();
    section.setSectionName("IELTS");
    
    sRepo.save(section);
    
    System.out.println(studnet.toString());
  }
}
