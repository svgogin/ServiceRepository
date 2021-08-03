package ru.svgogin.service.spark.exception;

public class EntityAlreadyExistsException extends RuntimeException {

  public EntityAlreadyExistsException(String message) {
    super(message);
  }
}
