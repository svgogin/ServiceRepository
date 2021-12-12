package ru.svgogin.service.spark.handler;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.svgogin.service.spark.errordto.ErrorDto;
import ru.svgogin.service.spark.errordto.ErrorDto.ErrorCode;
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.exception.NoSuchEntityException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger log = LoggerFactory
      .getLogger(RestResponseEntityExceptionHandler.class);

  @ExceptionHandler(value = {EntityAlreadyExistsException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    var bodyOfResponse = List.of(new ErrorDto(ErrorCode.ERROR001, ex.getMessage()));
    log.warn(ex.getMessage());
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(value = {NoSuchEntityException.class})
  protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
    var bodyOfResponse = List.of(new ErrorDto(ErrorCode.ERROR002, ex.getMessage()));
    log.warn(ex.getMessage());
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<Object> handleNotValid(ConstraintViolationException ex,
                                                  WebRequest request) {
    Iterable<ErrorDto> errors = ex.getConstraintViolations().stream()
        .map(this::toErrorDto).collect(Collectors.toList());
    return handleExceptionInternal(ex, errors,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                @NonNull HttpHeaders headers,
                                                                @NonNull HttpStatus status,
                                                                @NonNull WebRequest request) {
    Iterable<ErrorDto> errors = ex.getBindingResult().getAllErrors().stream()
        .map(this::toErrorDto).collect(Collectors.toList());

    return handleExceptionInternal(ex, errors,
        headers, status, request);
  }

  private ErrorDto toErrorDto(ObjectError objectError) {
    ErrorCode errorCode;
    var notBlankExists = Optional.ofNullable(objectError.getCodes())
        .map(codes -> Set.of(codes).contains("NotBlank"))
        .orElse(false);
    if (notBlankExists) {
      errorCode = ErrorCode.ERROR004;
    } else {
      errorCode = ErrorCode.ERROR003;
    }
    var message = String.format("%s (%s)", errorCode.label, ((FieldError) objectError).getField());
    log.warn(message);
    return new ErrorDto(errorCode, message);
  }

  private ErrorDto toErrorDto(ConstraintViolation<?> violation) {
    var message = String.format("%s (%s)", violation.getMessageTemplate(),
        violation.getInvalidValue());
    log.warn(message);
    return new ErrorDto(ErrorCode.ERROR003, message);
  }
}
