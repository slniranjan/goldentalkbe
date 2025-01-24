package com.goldentalk.gt.dto;

import com.goldentalk.gt.entity.AddressDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStudentRequest {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Expecting International format number. " +
            "Ex: +94xxxxxxxxxxxx Maximum length is 15")
    private String whatsAppNumber;

    @Size(min = 10, max = 12)
    private String nic;

    @Email
    @NotBlank
    private String email;

    private AddressDTO address;

    @NotNull
    private Integer sectionId;

    @NotNull
    private Integer courseId;

    @NotNull(message = "Payment information required for student registration")
    PaymentDetailsDTO payment;

    private Boolean earlyBird;

}
