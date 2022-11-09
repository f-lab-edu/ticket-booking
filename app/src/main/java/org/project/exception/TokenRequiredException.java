package org.project.exception;

public class TokenRequiredException extends RuntimeException {

  public TokenRequiredException() {
    super("Token required");
  }

}
