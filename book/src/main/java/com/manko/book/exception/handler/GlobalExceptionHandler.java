package com.manko.book.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.manko.book.dto.response.ErrorResponseDto;
import com.manko.book.enums.ErrorCode;
import com.manko.book.exception.MsBookException;
import jakarta.mail.MessagingException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MsBookException.class)
  protected ResponseEntity<ErrorResponseDto> handleMsBookException(
      MsBookException ex) {
    String message = ex.getMessage();
    return processError(BAD_REQUEST, message);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    FieldError fieldError = ex.getFieldErrors().get(0);
    String message = fieldError.getDefaultMessage();
    return processError(BAD_REQUEST, message);
  }

  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<ErrorResponseDto> handleMessagingException(MessagingException ex) {
    String message = ex.getMessage();
    return processError(INTERNAL_SERVER_ERROR, message);
  }

  private ResponseEntity<ErrorResponseDto> processError(HttpStatus status, String message) {
    log.error(message);
    ErrorResponseDto error = buildErrorResponseDto(message);
    return ResponseEntity.status(status.value()).body(error);
  }

  private ErrorResponseDto buildErrorResponseDto(String message) {
    return ErrorResponseDto.builder()
        .code(ErrorCode.valueOfMessage(message))
        .message(message)
        .timestamp(Instant.now())
        .build();
  }

}
