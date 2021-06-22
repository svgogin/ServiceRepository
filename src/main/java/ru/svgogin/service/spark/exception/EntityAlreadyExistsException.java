package ru.svgogin.service.spark.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This company already exists")
public class EntityAlreadyExistsException extends RuntimeException{
  private final String message;

  private static final Logger log = LoggerFactory.getLogger(EntityAlreadyExistsException.class);

  public EntityAlreadyExistsException(String message) {
    this.message = message;
    log.error(message);
  }

  @Override
  public String getMessage() {
    return message;
  }
}


