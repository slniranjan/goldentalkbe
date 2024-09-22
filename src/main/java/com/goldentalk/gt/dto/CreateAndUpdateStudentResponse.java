package com.goldentalk.gt.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class CreateAndUpdateStudentResponse {

  private Integer id;
  private String studentId;
  private String message;

}
