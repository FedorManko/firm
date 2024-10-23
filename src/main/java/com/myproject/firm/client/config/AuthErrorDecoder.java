package com.myproject.firm.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.firm.client.response.AuthErrorResponseDto;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthErrorDecoder implements ErrorDecoder {

  private final ObjectMapper mapper;

  @Override
  public Exception decode(String methodKey, Response response) {
    log.error(
        "Got an error with status: {} while requesting info from Auth service",
        response.status()
    );

    try (InputStream is = response.body().asInputStream()) {
      log.info("Trying to parse Auth service error body ...");
      AuthErrorResponseDto error = mapper.readValue(is, AuthErrorResponseDto.class);

      log.error("Got an error from Auth service: {}", error.toString());

      return new RuntimeException(error.getErrorDescription());
    } catch (Exception e) {
      log.error("Failed to parse otp error response body", e);
      return new RuntimeException("Got unknown error from Otp service " + e.getMessage());
    }
  }

}
