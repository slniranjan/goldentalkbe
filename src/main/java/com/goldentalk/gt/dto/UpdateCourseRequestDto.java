package com.goldentalk.gt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCourseRequestDto {

  @NotBlank
  @NotNull
  private String category;

  @NotBlank
  @NotNull
  private String name;

  @NotNull
  private Boolean installment;

  @Min(1000)
  private double fee;

  private double discount = 0.0;

}
