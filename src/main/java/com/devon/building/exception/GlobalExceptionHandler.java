package com.devon.building.exception;

import com.devon.building.model.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException e) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        List<String> details = new ArrayList<>();
        responseDTO.setDetail(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<String> details = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : error.getField() + " không hợp lệ")
                .toList();
        responseDTO.setMessage(details.isEmpty() ? "Dữ liệu không hợp lệ" : details.get(0));
        responseDTO.setDetail(details);
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<ResponseDTO> handleDataInvalidException(DataInvalidException e) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        responseDTO.setDetail(new ArrayList<>());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }
}
