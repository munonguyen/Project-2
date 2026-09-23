package com.devon.building.exception;

import java.io.Serial;

public class DataInvalidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3L;

    public DataInvalidException(String message) {
        super(message);
    }
}
