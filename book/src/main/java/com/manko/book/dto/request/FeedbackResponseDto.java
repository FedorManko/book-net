package com.manko.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDto {

  private Double note;
  private String comment;
  private boolean ownFeedback;
}
