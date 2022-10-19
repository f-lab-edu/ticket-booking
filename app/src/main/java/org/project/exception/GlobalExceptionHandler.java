package org.project.exception;

import org.project.common.dto.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidRefreshTokenException.class)
  public ResponseEntity<GenericErrorResponse> handleInvalidRefreshTokenException(
      InvalidRefreshTokenException e) {
    GenericErrorResponse response = new GenericErrorResponse();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    response.setMessage(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

}
