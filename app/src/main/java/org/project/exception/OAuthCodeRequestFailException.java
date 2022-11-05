package org.project.exception;

public class OAuthCodeRequestFailException extends RuntimeException {

  public OAuthCodeRequestFailException(String message) {
    super(message);
  }

  public OAuthCodeRequestFailException(String message, Throwable cause) {
    super(message, cause);
  }

  public OAuthCodeRequestFailException(Throwable cause) {
    super(cause);
  }

  public OAuthCodeRequestFailException() {
    super();
  }

}
