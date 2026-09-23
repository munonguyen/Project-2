package com.devon.building.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomerDTO  {

    Long id;
    @NotBlank(message = "Full name must not be blank")
    String fullName;

    String email;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
        regexp = "^(03|05|07|08|09)\\d{8}$", 
        message = "Phone number is invalid (must be 10 digits starting with 03, 05, 07, 08, or 09)"
    )
    String phoneNumber;

    @NotBlank(message = "Demand must not be blank")
    String demand;

    String companyName;

    @NotBlank(message="Status must not be blank")
    String status;




}
