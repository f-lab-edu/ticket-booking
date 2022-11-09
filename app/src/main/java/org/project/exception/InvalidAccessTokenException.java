package org.project.exception;

public class InvalidAccessTokenException extends RuntimeException {

  public InvalidAccessTokenException() {
    super("Invalid access token");
  }

}
