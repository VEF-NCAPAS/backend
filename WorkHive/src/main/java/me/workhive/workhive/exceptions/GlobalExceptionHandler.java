package me.workhive.workhive.exceptions;

import me.workhive.workhive.domain.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlerResourceNotFound(ResourceNotFoundException e){
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicatedResourceException.class)
    public ResponseEntity<ApiErrorResponse> handlerDuplicatedResourceException(DuplicatedResourceException e){
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handlerInvalidCredentialsException(InvalidCredentialsException e){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handlerAccessDeniedException(AccessDeniedException e){
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .uri(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath())
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .time(LocalDate.now())
                        .build());
    }

    public ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, Object message){
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return  ResponseEntity
                .status(status)
                .body(ApiErrorResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDate.now())
                        .build());
    }
}
