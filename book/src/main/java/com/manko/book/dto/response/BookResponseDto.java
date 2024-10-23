package com.manko.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

  private Long id;
  private String title;
  private String authorName;
  private String isbn;
  private String synopsis;
  private String owner;
  private byte[] cover;
  private double rate;
  private boolean archived;
  private boolean shareable;

}
