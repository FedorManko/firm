package com.myproject.firm.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthErrorResponseDto {

  private String error;

  @JsonProperty("error_description")
  private String errorDescription;

}
