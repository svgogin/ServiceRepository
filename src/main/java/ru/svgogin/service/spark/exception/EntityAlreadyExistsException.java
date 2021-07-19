package ru.svgogin.service.spark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This company already exists")
public class EntityAlreadyExistsException extends RuntimeException {
  private final String message;

  public EntityAlreadyExistsException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}


