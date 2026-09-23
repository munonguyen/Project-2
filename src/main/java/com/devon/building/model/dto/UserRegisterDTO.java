package com.devon.building.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "Họ tên không được để trống")
    @JsonAlias({"fullName", "fullname"})
    private String fullname;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(regexp = "^[^@]*$", message = "Tên đăng nhập không được là địa chỉ email")
    @JsonProperty("username")
    @JsonAlias({"userName", "username"})
    private String userName;

    @JsonProperty("phone_number")
    @JsonAlias({"phoneNumber", "phone", "phone_number"})
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @JsonProperty("retype_password")
    @JsonAlias({"confirmPassword", "retypePassword", "retype_password"})
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("facebook_account_id")
    @Builder.Default
    private Long facebookAccountId = 0L;

    @JsonProperty("google_account_id")
    @Builder.Default
    private Long googleAccountId = 0L;

    @JsonProperty("role_id")
    private Long roleId;
}
