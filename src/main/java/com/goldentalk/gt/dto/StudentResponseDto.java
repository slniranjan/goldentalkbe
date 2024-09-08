package com.goldentalk.gt.dto;

import java.util.Set;
import com.goldentalk.gt.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponseDto {

  private String studentId;
 
  private String internalId;
  
  private String firstName;
  
  private String middleName;
  
  private String lastName;
  
  private String dob;
  
  private String whatsappNum;
  
  private Address address;
  
  private Set<String> sectionIds;
  
  private Set<String> courseIds;
}
