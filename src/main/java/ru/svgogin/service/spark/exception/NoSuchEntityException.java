package ru.svgogin.service.spark.exception;

public class NoSuchEntityException extends RuntimeException {

  public NoSuchEntityException(String message) {
    super(message);
  }
}
