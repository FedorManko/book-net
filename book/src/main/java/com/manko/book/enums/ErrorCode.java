package com.manko.book.enums;

import com.manko.book.util.ErrorCodeConstants;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  ROLE_WAS_NOT_INITIALIZED(ErrorCodeConstants.ROLE_WAS_NOT_INITIALIZED),
  INVALID_TOKEN(ErrorCodeConstants.INVALID_TOKEN),
  TOKEN_EXPIRED(ErrorCodeConstants.TOKEN_EXPIRED),
  USER_NOT_FOUND(ErrorCodeConstants.USER_NOT_FOUND),
  WRONG_EMAIL_FORMAT(ErrorCodeConstants.WRONG_EMAIL_FORMAT),
  BOOK_NOT_FOUND(ErrorCodeConstants.BOOK_NOT_FOUND),
  UNEXPECTED_ERROR_OCCURRED(ErrorCodeConstants.UNEXPECTED_ERROR_OCCURRED);

  private final String message;

  public static ErrorCode valueOfMessage(String message) {
    return Stream.of(values())
        .filter(errorCode -> errorCode.getMessage().equals(message))
        .findFirst()
        .orElse(UNEXPECTED_ERROR_OCCURRED);
  }

}
