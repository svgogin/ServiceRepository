package ru.svgogin.service.spark.handler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.svgogin.service.spark.errordto.ErrorDto.ErrorCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import ru.svgogin.service.spark.errordto.ErrorDto;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException ex) throws IOException {

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
}
