package com.twitter.api.exception;

import com.twitter.api.exception.AppException;
import com.twitter.api.exception.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Uygulamaya özel hatalar
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handleAppException(AppException ex) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "Uygulama kaynaklı hata"
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Genel (beklenmeyen) hatalar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                "Bilinmeyen bir hata oluştu.",
                "Sunucu hatası"
        );
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
