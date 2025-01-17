package com.goldentalk.gt.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.goldentalk.gt.entity.Address;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class StudentResponseDto {

  private String studentId;
 
  private String firstName;
  
  private String middleName;
  
  private String lastName;
  
  private String whatsAppNum;

  private String nic;

  private String email;

  private Address address;
  
  private Set<SectionResponseDTO> section;
//  private Set<String> section;

  private Set<CourseResponseDto> course;
//  private Set<String> course;

  private List<PaymentDetailsDTO> payments = new ArrayList<PaymentDetailsDTO>();
  
}
