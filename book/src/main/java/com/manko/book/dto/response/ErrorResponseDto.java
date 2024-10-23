package com.manko.book.dto.response;

import com.manko.book.enums.ErrorCode;
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
