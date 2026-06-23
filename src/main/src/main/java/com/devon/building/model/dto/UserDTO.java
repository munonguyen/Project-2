package com.devon.building.model.dto;

import com.devon.building.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String userName;
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;
    @Size(min = 3)
    private String password;
    private Integer status;
    private MultipartFile fileData;
    private Map<String, String> roleDTO;
    private String roleCode;
    private String phone;

    private String base64Image;
    private String imageName;

    public void initRoles() {
        this.roleDTO = Arrays.stream(UserRole.values()).collect(Collectors.toMap(UserRole::getCode, UserRole::getLabel));
    }
}
