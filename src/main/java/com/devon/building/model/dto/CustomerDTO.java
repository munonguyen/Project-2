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
    @NotBlank(message = "Họ tên không được để trống")
    String fullName;

    String email;

    @JsonAlias({"customerPhone", "phone"})
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
        regexp = "^(03|05|07|08|09)\\d{8}$", 
        message = "Số điện thoại không hợp lệ (phải có 10 chữ số, bắt đầu bằng 03, 05, 07, 08 hoặc 09)"
    )
    String phoneNumber;

    @NotBlank(message = "Nhu cầu không được để trống")
    String demand;

    String companyName;

    String status;

}
