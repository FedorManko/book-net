package com.manko.book.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailTemplateName {
  ACTIVATE_ACCOUNT("activate account");

  private final String name;
}
