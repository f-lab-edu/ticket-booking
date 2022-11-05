package org.project.exception;

import org.project.common.dto.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(OAuthCodeRequestFailException.class)
  public ResponseEntity<GenericErrorResponse> handleOAuthCodeRequestFailException(
      OAuthCodeRequestFailException e) {
    GenericErrorResponse errorResponse = new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

}
