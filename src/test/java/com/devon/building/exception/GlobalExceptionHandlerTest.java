package com.devon.building.exception;

import com.devon.building.model.dto.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void entityNotFoundMapsToNotFound() {
        ResponseEntity<ResponseDTO> response =
                handler.handleEntityNotFoundException(new EntityNotFoundException("Building not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Building not found");
    }

    @Test
    void dataIntegrityViolationMapsToConflict() {
        ResponseEntity<ResponseDTO> response = handler.handleDataIntegrityViolationException(
                new DataIntegrityViolationException("constraint violation")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("dữ liệu");
    }

    @Test
    void invalidRequestMapsToBadRequest() {
        ResponseEntity<ResponseDTO> response =
                handler.handleInvalidRequestException(new InvalidRequestException("Invalid request"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid request");
    }

    @Test
    void unexpectedExceptionMapsToInternalServerErrorWithoutLeakingDetails() {
        ResponseEntity<ResponseDTO> response =
                handler.handleUnexpectedException(new RuntimeException("sensitive database detail"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).doesNotContain("sensitive database detail");
    }
}
