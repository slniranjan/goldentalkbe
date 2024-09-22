package com.goldentalk.gt.dto;

import com.goldentalk.gt.entity.AddressDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAndUpdateStudentRequest {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

//    private String dob;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Expecting International format number. " +
            "Ex: +94xxxxxxxxxxxx Maximum length is 15")
    private String whatsAppNumber;

    private AddressDTO address;

    @NotNull
    private Integer sectionId;

    @NotNull
    private Integer courseId;

    @NotNull(message = "Payment information required for student registration")
    PaymentDetailsDTO paymentDto;

}
