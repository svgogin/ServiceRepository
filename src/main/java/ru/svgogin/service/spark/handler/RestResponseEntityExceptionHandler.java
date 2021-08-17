package ru.svgogin.service.spark.handler;

import static java.util.Arrays.asList;

import java.util.Objects;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.svgogin.service.spark.errordto.ErrorDto;
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.exception.NoSuchEntityException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger log = LoggerFactory
      .getLogger(RestResponseEntityExceptionHandler.class);

  @ExceptionHandler(value = {EntityAlreadyExistsException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    log.warn(message);
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR001, message);
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(value = {NoSuchEntityException.class})
  protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    log.warn(message);
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR002, message);
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<Object> handleNotValid(RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR003, message);
    log.warn(message);
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                @NonNull HttpHeaders headers,
                                                                @NonNull HttpStatus status,
                                                                @NonNull WebRequest request) {
    var error = ex.getBindingResult().getFieldErrors().iterator().next();
    var errorField = error.getField();
    //This is a bullshit implementation, but I have no idea, how to improve it
    var notBlankExists = asList(Objects.requireNonNull(error.getCodes())).contains("NotBlank");
    if (notBlankExists) {
      var message = "Request parameter is required: " + errorField;
      var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR004, message);
      log.warn(message);
      return handleExceptionInternal(ex, bodyOfResponse,
          headers, status, request);
    }
    var message = "Invalid format of request parameter: " + errorField;
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR003, message);
    log.warn(message);
    return handleExceptionInternal(ex, bodyOfResponse,
        headers, status, request);
  }
}
