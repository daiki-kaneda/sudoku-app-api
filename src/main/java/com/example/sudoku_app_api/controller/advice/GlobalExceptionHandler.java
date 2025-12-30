package com.example.sudoku_app_api.controller.advice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.sudoku_app_api.controller.dto.ApiErrorResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request: ビジネスロジックエラー（3件制限、1分制限など）
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiErrorResponse> handleBusinessError(RuntimeException e) {
        log.warn("Business logic error: {}", e.getMessage());
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "BUSINESS_RULE_VIOLATION");
    }

    // 404 Not Found: リソース不在
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return createResponse(HttpStatus.NOT_FOUND, e.getMessage(), "RESOURCE_NOT_FOUND");
    }

    // 400 Bad Request: バリデーションエラー（入力形式不正）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation error: {}", errors);

        ApiErrorResponse response = new ApiErrorResponse(
            "入力内容に不備があります。",
            "VALIDATION_ERROR",
            Instant.now(),
            errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    // 500 Internal Server Error: 予期せぬエラー
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalError(Exception e) {
        log.error("Unexpected error occurred", e);
        
        return createResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "サーバー内部でエラーが発生しました。", 
            "INTERNAL_SERVER_ERROR"
        );
    }

    private ResponseEntity<ApiErrorResponse> createResponse(HttpStatus status, String message, String errorCode) {
        return ResponseEntity
            .status(status)
            .body(ApiErrorResponse.of(message, errorCode));
    }
}
