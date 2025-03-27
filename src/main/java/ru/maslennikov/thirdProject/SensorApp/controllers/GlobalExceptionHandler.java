package ru.maslennikov.thirdProject.SensorApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedException;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработка кастомного исключения NotCreatedException
    @ExceptionHandler(NotCreatedException.class)
    public ResponseEntity<Map<String, String>> handleNotCreatedException(NotCreatedException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        errorResponse.put("timestamp", Instant.now().toString()); // Используем ISO 8601 формат

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException ex) {
        return "Constraint violation: " + ex.getMessage();
    }
}
