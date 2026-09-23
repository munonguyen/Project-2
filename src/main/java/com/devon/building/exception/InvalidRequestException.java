package com.devon.building.exception;

import java.io.Serial;

public class InvalidRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2L;
    public InvalidRequestException(String message){
        super(message);
    }
}
