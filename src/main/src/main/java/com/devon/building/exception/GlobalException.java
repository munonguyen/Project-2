package com.devon.building.exception;

import com.devon.building.CustomException.DataBuildingInvalidException;
import com.devon.building.model.dto.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
