package com.featureflagmanagment.featureflagmanagment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AplicationException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
