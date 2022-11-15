package org.project.exception;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException() {
    super("Member not found");
  }

  public MemberNotFoundException(String email) {
    super("Member not found: " + email);
  }

}
