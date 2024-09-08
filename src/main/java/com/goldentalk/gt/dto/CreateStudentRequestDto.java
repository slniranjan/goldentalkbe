package com.goldentalk.gt.dto;

import java.util.List;
import com.goldentalk.gt.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequestDto {

  private String firstName;

  private String middleName;
  
  private String lastName;
  
  private String dob;
  
  private String whatsAppNumber;
  
  private Address address;
  
  private List<Integer> sectionId;
  
  private List<Integer> courseIds;
  
}
