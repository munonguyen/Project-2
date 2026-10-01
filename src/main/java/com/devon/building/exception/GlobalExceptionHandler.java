package com.devon.building.exception;

import com.devon.building.model.dto.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataBuildingInvalidException.class)
    public ResponseEntity<ResponseDTO> handleDataBuildingInvalidException(DataBuildingInvalidException e) {
        if (e.getErrorResponseDTO() != null) {
            return ResponseEntity.badRequest().body(e.getErrorResponseDTO());
        }
        return response(HttpStatus.BAD_REQUEST, e.getMessage(), List.of());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ResponseDTO> handleInvalidRequestException(InvalidRequestException e) {
        return response(HttpStatus.BAD_REQUEST, e.getMessage(), List.of());
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<ResponseDTO> handleDataInvalidException(DataInvalidException e) {
        return response(HttpStatus.BAD_REQUEST, e.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> details = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage() != null
                        ? error.getDefaultMessage()
                        : error.getField() + " không hợp lệ")
                .toList();
        String message = details.isEmpty() ? "Dữ liệu không hợp lệ" : details.get(0);
        return response(HttpStatus.BAD_REQUEST, message, details);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        return response(HttpStatus.NOT_FOUND, e.getMessage(), List.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String invalidValue = String.valueOf(e.getValue());
        return response(
                HttpStatus.BAD_REQUEST,
                "ID không hợp lệ",
                List.of("Giá trị '" + invalidValue + "' không đúng định dạng yêu cầu")
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage() == null || e.getMessage().isBlank()
                ? "Dữ liệu đầu vào không hợp lệ"
                : e.getMessage();
        return response(HttpStatus.BAD_REQUEST, message, List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return response(
                HttpStatus.CONFLICT,
                "Không thể thực hiện thao tác do dữ liệu đang được liên kết hoặc bị trùng lặp",
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleUnexpectedException(Exception e) {
        log.error("Unhandled application exception", e);
        return response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.",
                List.of()
        );
    }

    private ResponseEntity<ResponseDTO> response(HttpStatus status, String message, List<String> details) {
        ResponseDTO body = new ResponseDTO();
        body.setMessage(message);
        body.setDetail(details == null ? new ArrayList<>() : details);
        return ResponseEntity.status(status).body(body);
    }
}
