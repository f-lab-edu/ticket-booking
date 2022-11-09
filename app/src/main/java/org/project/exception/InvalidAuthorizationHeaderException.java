package org.project.exception;

public class InvalidAuthorizationHeaderException extends RuntimeException {

  public InvalidAuthorizationHeaderException() {
    super("Invalid authorization header");
  }

}
