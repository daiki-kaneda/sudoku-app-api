package com.example.sudoku_app_api.controller.dto;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        String message,
        String errorCode,
        Instant timeStamp,
        Map<String, String> details) {
    public static ApiErrorResponse of(String message, String errorCode) {
        return new ApiErrorResponse(message, errorCode, Instant.now(), null);
    }
}
