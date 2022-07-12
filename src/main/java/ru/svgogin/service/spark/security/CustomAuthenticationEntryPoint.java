package ru.svgogin.service.spark.security;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.svgogin.service.spark.errordto.ErrorDto.ErrorCode.ERROR007;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ru.svgogin.service.spark.errordto.ErrorDto;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException ex)
      throws IOException {

    log.warn("{} {} request failed because of: {}",
        request.getMethod(),
        request.getRequestURI(),
        ERROR007.label);

    var bodyOfResponse = List.of(new ErrorDto(ERROR007));

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getOutputStream().println(objectMapper.writeValueAsString(bodyOfResponse));
    response.setStatus(UNAUTHORIZED.value());

  }
}
