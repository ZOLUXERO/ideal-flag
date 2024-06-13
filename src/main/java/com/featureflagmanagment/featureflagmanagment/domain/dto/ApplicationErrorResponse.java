package com.featureflagmanagment.featureflagmanagment.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationErrorResponse {
    private final String guid;
    private final String errorCode;
    private final String errorMessage;
    private final Integer statusCode;
    private final String statusName;
    private final String path;
    private final String method;
    private final LocalDateTime timestamp;
}
