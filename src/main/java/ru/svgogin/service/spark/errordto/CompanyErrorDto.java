package ru.svgogin.service.spark.errordto;

import org.springframework.lang.NonNull;

public final class CompanyErrorDto {
  @NonNull
  private final ErrorCode code;
  @NonNull
  private final String message;

  public CompanyErrorDto(@NonNull ErrorCode code, @NonNull String message) {

    this.code = code;
    this.message = message;
  }

  @NonNull
  public ErrorCode getCode() {
    return code;
  }

  @NonNull
  public String getMessage() {
    return message;
  }

  public enum ErrorCode {
    ERROR001,
    ERROR002
  }
}
