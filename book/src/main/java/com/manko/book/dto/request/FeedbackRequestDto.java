package com.manko.book.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDto {

  @Positive(message = "200")
  @Min(value = 0, message = "201")
  @Max(value = 5, message = "202")
  private Double note;
  @NotNull(message = "203")
  @NotEmpty(message = "203")
  @NotBlank(message = "203")
  private String comment;
  @NotNull(message = "204")
  private Long bookId;

}
