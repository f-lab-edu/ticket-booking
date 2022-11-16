package org.project.exception;

public class ConcertNotFoundException extends NotFoundException {

  public ConcertNotFoundException() {
    super("Concert not found");
  }

  public ConcertNotFoundException(Long id) {
    super("Concert not found for given id: " + id);
  }

}
