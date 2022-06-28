package ru.svgogin.service.spark.handler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.svgogin.service.spark.errordto.ErrorDto;
import ru.svgogin.service.spark.errordto.ErrorDto.ErrorCode;
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.exception.NoSuchEntityException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements
    AuthenticationFailureHandler {
  private static final Logger log = LoggerFactory
      .getLogger(RestResponseEntityExceptionHandler.class);

  private static void accept(ObjectError error) {
    log.warn(error.toString(), error);
  }

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
    ex.getConstraintViolations().forEach(
        constraintViolation -> log.warn(constraintViolation.toString()));
    return handleExceptionInternal(ex, errors,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex,
                                                      ServletWebRequest request) {
    var bodyOfResponse = List.of(new ErrorDto(ErrorDto.ErrorCode.ERROR006,
        ErrorDto.ErrorCode.ERROR006.label));

    Authentication auth
        = SecurityContextHolder.getContext().getAuthentication();


    if (auth != null) {
      log.warn("User: {} has no privileges to access the protected URL: {} with roles: {}",
          auth.getName(), request.getRequest().getRequestURI(), auth.getAuthorities());
    }
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                @NonNull HttpHeaders headers,
                                                                @NonNull HttpStatus status,
                                                                @NonNull WebRequest request) {
    Iterable<ErrorDto> errors = ex.getBindingResult().getAllErrors().stream()
        .map(this::toErrorDto).collect(Collectors.toList());
    ex.getBindingResult().getAllErrors().forEach(objectError -> log.warn(objectError.toString()));
    return handleExceptionInternal(ex, errors,
        headers, status, request);
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
                                      HttpServletResponse response,
                                      AuthenticationException ex) throws IOException {
    final ObjectMapper objectMapper = new ObjectMapper();

    log.warn("Request: "
             + request.getMethod()
             + request.getRequestURI()
             + " failed because of "
             + ErrorCode.ERROR005.label);

    var bodyOfResponse = List.of(new ErrorDto(ErrorCode.ERROR005, ErrorCode.ERROR005.label));
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getOutputStream().println(objectMapper.writeValueAsString(bodyOfResponse));
    response.setStatus(UNAUTHORIZED.value());
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
    return new ErrorDto(errorCode, message);
  }

  private ErrorDto toErrorDto(ConstraintViolation<?> violation) {
    var message = String.format("%s (%s)", violation.getMessageTemplate(),
        violation.getInvalidValue());
    return new ErrorDto(ErrorCode.ERROR003, message);
  }
}
