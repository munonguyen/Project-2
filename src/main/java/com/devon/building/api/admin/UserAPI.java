package com.devon.building.api.admin;

import com.devon.building.model.dto.PasswordDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.UserDTO;
import com.devon.building.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAPI {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ResponseDTO<Void>> createUser(
      @Valid @ModelAttribute UserDTO user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return validationError(bindingResult);
    }
    userService.save(user);
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();
    responseDTO.setMessage("Tạo người dùng thành công");
    return ResponseEntity.ok(responseDTO);
  }

  @PutMapping
  public ResponseEntity<ResponseDTO<Void>> updateUser(
      @Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return validationError(bindingResult);
    }
    userService.update(userDTO);
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();
    responseDTO.setMessage("Cập nhật người dùng thành công");
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping
  public ResponseEntity<ResponseDTO<Void>> deleteUsers(@RequestBody List<Long> idList) {
    if (!idList.isEmpty()) {
      userService.delete(idList);
    }
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();
    responseDTO.setMessage("Xóa người dùng thành công");
    return ResponseEntity.ok(responseDTO);
  }

  @PutMapping("/password/{id}")
  public ResponseEntity<ResponseDTO<Void>> updateUser(
      @PathVariable Long id, @RequestBody PasswordDTO passwordDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();
    return ResponseEntity.ok(responseDTO);
  }

  private ResponseEntity<ResponseDTO<Void>> validationError(BindingResult bindingResult) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();
    responseDTO.setMessage("Dữ liệu không hợp lệ");
    responseDTO.setDetail(
        bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList());
    return ResponseEntity.badRequest().body(responseDTO);
  }
}
