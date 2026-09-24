package com.devon.building.api.admin;

import com.devon.building.model.dto.PasswordDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.UserDTO;
import com.devon.building.model.dto.UserLoginDTO;
import com.devon.building.model.request.AuthenticationRequest;
import com.devon.building.model.response.AuthenticationResponse;
import com.devon.building.service.AuthenticationService;
import com.devon.building.service.UserService;
import com.devon.building.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/users", "${api.prefix:/api}/users"})
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            AuthenticationRequest authRequest = AuthenticationRequest.builder()
                    .username(userLoginDTO.getUserName())
                    .password(userLoginDTO.getPassword())
                    .build();
            AuthenticationResponse authResponse = authenticationService.authenticate(authRequest);

            responseDTO.setMessage("Đăng nhập thành công");
            responseDTO.setData(authResponse.getToken());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage() != null ? e.getMessage() : "Tên đăng nhập hoặc mật khẩu không chính xác");
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody com.devon.building.model.dto.UserRegisterDTO userRegisterDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRetypePassword())) {
                responseDTO.setMessage("Mật khẩu nhập lại không khớp!");
                return ResponseEntity.badRequest().body(responseDTO);
            }
            User user = userService.register(userRegisterDTO);

            com.devon.building.model.response.UserInfoResponse userInfoResponse = com.devon.building.model.response.UserInfoResponse.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .fullName(user.getFullName())
                    .phone(user.getPhone())
                    .address(userRegisterDTO.getAddress())
                    .dateOfBirth(userRegisterDTO.getDateOfBirth())
                    .role(user.getUserRole())
                    .facebookAccountId(userRegisterDTO.getFacebookAccountId())
                    .googleAccountId(userRegisterDTO.getGoogleAccountId())
                    .build();

            responseDTO.setMessage("Đăng ký người dùng thành công");
            responseDTO.setData(userInfoResponse);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage() != null ? e.getMessage() : "Đăng ký thất bại");
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            userService.save(userDTO);
            responseDTO.setMessage("Thêm người dùng thành công");
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            userService.update(userDTO);
            responseDTO.setMessage("Cập nhật người dùng thành công");
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUsers(@RequestBody List<Long> idList) {
        if (!idList.isEmpty()) {
            userService.delete(idList);
        }
        return ResponseEntity.ok().body("{ \"message\": \"Xóa người dùng thành công\" }");
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody PasswordDTO passwordDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            userService.updatePassword(id, passwordDTO);
            responseDTO.setMessage("Đổi mật khẩu thành công!");
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
