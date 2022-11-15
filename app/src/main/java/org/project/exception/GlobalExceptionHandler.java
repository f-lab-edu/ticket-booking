package org.project.exception;

import org.project.dto.GenericErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(OAuthCodeRequestFailException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public GenericErrorResponse handleOAuthCodeRequestFailException(
      OAuthCodeRequestFailException e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
  }

  @ExceptionHandler(InvalidRefreshTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public GenericErrorResponse handleInvalidRefreshTokenException(
      InvalidRefreshTokenException e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
  }

  @ExceptionHandler(InvalidAccessTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public GenericErrorResponse handleExpiredAccessTokenException(
      InvalidAccessTokenException e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
  }

  @ExceptionHandler(TokenRequiredException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public GenericErrorResponse handleTokenRequiredException(
      TokenRequiredException e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
  }

  @ExceptionHandler(InvalidAuthorizationHeaderException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public GenericErrorResponse handleInvalidAuthorizationHeaderException(
      InvalidAuthorizationHeaderException e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
  }

  @ExceptionHandler(MemberNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public GenericErrorResponse handleMemberNotFoundException(MemberNotFoundException e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()
    );
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    GenericErrorResponse errorResponse = new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public GenericErrorResponse handleException(Exception e) {
    return new GenericErrorResponse(
        String.valueOf(System.currentTimeMillis()), HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage()
    );
  }
}
