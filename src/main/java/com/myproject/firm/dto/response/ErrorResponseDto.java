package com.myproject.firm.dto.response;

import com.myproject.firm.enums.ErrorCode;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

  private ErrorCode code;

  private String message;

  private Instant timestamp;

}
