package com.devon.building.exception;

import com.devon.building.model.dto.ResponseDTO;

public class DataBuildingInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private ResponseDTO errorResponseDTO;

    public DataBuildingInvalidException(String message) {
        super(message);
    }

    public DataBuildingInvalidException(ResponseDTO errorResponseDTO) {
        super(errorResponseDTO.getMessage());
        this.errorResponseDTO = errorResponseDTO;
    }

    public DataBuildingInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseDTO getErrorResponseDTO() {
        return errorResponseDTO;
    }

    public void setErrorResponseDTO(ResponseDTO errorResponseDTO) {
        this.errorResponseDTO = errorResponseDTO;
    }
}
