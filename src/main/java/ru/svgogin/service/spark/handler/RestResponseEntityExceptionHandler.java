package ru.svgogin.service.spark.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @ExceptionHandler(value = {
      EntityAlreadyExistsException.class
  })

  protected ResponseEntity<Object> handleConflict(
      RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    log.info(message);
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR001, message);
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(value = {
      NoSuchEntityException.class
  })

  protected ResponseEntity<Object> handleNotFound(
      RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    log.info(message);
    var bodyOfResponse = new ErrorDto(ErrorDto.ErrorCode.ERROR002, message);
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}
