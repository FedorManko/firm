package com.myproject.firm.enums;

import com.myproject.firm.util.ErrorCodeConstants;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  EMPLOYEE_RECORD_NOT_FOUND(ErrorCodeConstants.EMPLOYEE_RECORD_NOT_FOUND),
  UNEXPECTED_ERROR_OCCURRED(ErrorCodeConstants.UNEXPECTED_ERROR_OCCURRED);

  private final String message;

  public static ErrorCode valueOfMessage(String message) {
    return Stream.of(values())
        .filter(errorCode -> errorCode.getMessage().equals(message))
        .findFirst()
        .orElse(UNEXPECTED_ERROR_OCCURRED);
  }

}
