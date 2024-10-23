package com.manko.book.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodeConstants {

  public static final String ROLE_WAS_NOT_INITIALIZED = "ROLE USER was not initialized";
  public static final String INVALID_TOKEN = "Invalid token";
  public static final String TOKEN_EXPIRED =
      "Activation token has expired. A new token has been send";
  public static final String USER_NOT_FOUND = "User not found";
  public static final String BOOK_NOT_FOUND = "Book not found";
  public static final String WRONG_EMAIL_FORMAT = "Wrong email format";
  public static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error has occurred";
}
