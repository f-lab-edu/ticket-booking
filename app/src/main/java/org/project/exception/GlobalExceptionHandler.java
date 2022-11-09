package org.project.exception;

import org.project.dto.GenericErrorResponse;
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

  @ExceptionHandler(InvalidRefreshTokenException.class)
  public ResponseEntity<GenericErrorResponse> handleInvalidRefreshTokenException(
      InvalidRefreshTokenException e) {
    GenericErrorResponse response = new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(InvalidAccessTokenException.class)
  public ResponseEntity<GenericErrorResponse> handleExpiredAccessTokenException(
      InvalidAccessTokenException e) {
    GenericErrorResponse response = new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(TokenRequiredException.class)
  public ResponseEntity<GenericErrorResponse> handleTokenRequiredException(
      TokenRequiredException e) {
    GenericErrorResponse response = new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(InvalidAuthorizationHeaderException.class)
  public ResponseEntity<GenericErrorResponse> handleInvalidAuthorizationHeaderException(
      InvalidAuthorizationHeaderException e) {
    GenericErrorResponse response = new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }
}
