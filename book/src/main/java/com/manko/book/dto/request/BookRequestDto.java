package com.manko.book.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {

  private Long id;

  @NotNull(message = "100")
  @NotEmpty(message = "100")
  private String title;

  @NotNull(message = "101")
  @NotEmpty(message = "101")
  private String authorName;

  @NotNull(message = "102")
  @NotEmpty(message = "102")
  private String isbn;

  @NotNull(message = "103")
  @NotEmpty(message = "103")
  private String synopsis;

  private boolean shareable;

}