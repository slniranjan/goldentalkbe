package com.goldentalk.gt.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.goldentalk.gt.entity.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StudentResponseDto {

  private String studentId;
 
  private String firstName;
  
  private String middleName;
  
  private String lastName;
  
  private String whatsAppNum;
  
  private Address address;
  
  private Set<String> section;
  
  private Set<String> course;
  
  private List<PaymentDetailsDTO> payments = new ArrayList<PaymentDetailsDTO>();
  
}
