package com.devon.building.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
