package com.devon.building.exception;

import com.devon.building.CustomException.DataBuildingInvalidException;
import com.devon.building.model.dto.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(DataBuildingInvalidException.class)
    public ResponseEntity<Object> handleDataBuildingInvalidException(DataBuildingInvalidException e) {
        if (e.getErrorResponseDTO() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorResponseDTO());
        }
        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setMessage(e.getMessage());
        List<String> details = new ArrayList<>();
        details.add(e.getMessage());
        errorResponse.setDetail(details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setMessage("Dữ liệu đầu vào không hợp lệ (Ví dụ: File ảnh bị lỗi định dạng)");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setMessage("ID không hợp lệ");
        List<String> details = new ArrayList<>();
        details.add("Giá trị '" + e.getValue() + "' không đúng định dạng số");
        errorResponse.setDetail(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e) {
        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setMessage("Không thể thực hiện thao tác do dữ liệu đang được liên kết (VD: đang nằm trong Đơn Hàng)!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
