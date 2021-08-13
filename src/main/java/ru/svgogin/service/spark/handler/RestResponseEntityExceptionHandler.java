package ru.svgogin.service.spark.handler;

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

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                @NonNull HttpHeaders headers,
                                                                @NonNull HttpStatus status,
                                                                @NonNull WebRequest request) {
    var errorField = ex.getBindingResult().getFieldErrors().iterator().next().getField();
    var message = "Invalid format of request parameter: " + errorField;
    //toDO If a parameter is not passed in the request, I don't know,
    // how to write a condition to change the message.
    // In both cases handleMethodArgumentNotValid is invoked instead of
    // handleMissingServletRequestParameter
    // (when a parameter is missing)
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR003, message);
    log.warn(message);
    return handleExceptionInternal(ex, bodyOfResponse,
        headers, status, request);
  }
}
