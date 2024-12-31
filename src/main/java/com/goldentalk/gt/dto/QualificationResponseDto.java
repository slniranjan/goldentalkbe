package com.goldentalk.gt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualificationResponseDto {
    private Integer id;

    private String qualification;

    private String institute;
}
