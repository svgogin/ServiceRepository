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
import ru.svgogin.service.spark.errordto.CompanyErrorDto;
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.exception.NoSuchEntityException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger log = LoggerFactory
      .getLogger(RestResponseEntityExceptionHandler.class);

  @ExceptionHandler(value
                        = {EntityAlreadyExistsException.class, NoSuchEntityException.class})
  protected ResponseEntity<Object> handleConflict(
      RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    log.info(message);
    if (ex instanceof EntityAlreadyExistsException) {
      var bodyOfResponse = new CompanyErrorDto(CompanyErrorDto.ErrorCode.ERROR001, message);
      return handleExceptionInternal(ex, bodyOfResponse,
          new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    var bodyOfResponse = new CompanyErrorDto(CompanyErrorDto.ErrorCode.ERROR002, message);
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}
