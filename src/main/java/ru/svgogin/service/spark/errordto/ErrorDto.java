package ru.svgogin.service.spark.errordto;

public final class ErrorDto {
  private final ErrorCode code;
  private final String message;

  public ErrorDto(ErrorCode code, String message) {

    this.code = code;
    this.message = message;
  }

  public ErrorCode getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public enum ErrorCode {
    ERROR001("EntityAlreadyExists"),
    ERROR002("NoSuchEntity"),
    ERROR003("InvalidRequestFormat"),
    ERROR004("MissingParameter");

    public final String label;

    ErrorCode(String label) {
      this.label = label;
    }
  }
}

