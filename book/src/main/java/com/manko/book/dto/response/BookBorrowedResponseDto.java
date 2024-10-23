package com.manko.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookBorrowedResponseDto {

  private Long id;
  private String title;
  private String authorName;
  private String isbn;
  private double rate;
  private boolean returned;
  private boolean returnApproved;

}
