package com.manko.book.controller;

import com.manko.book.dto.request.FeedbackRequestDto;
import com.manko.book.dto.request.FeedbackResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

  private final FeedbackService feedbackService;

  @PostMapping
  public ResponseEntity<Long> saveFeedback(
      @Valid @RequestBody FeedbackRequestDto request,
      Authentication connectedUser
  ) {
    Long response = feedbackService.saveFeedback(request, connectedUser);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/book/{book-id}")
  public ResponseEntity<PageResponse<FeedbackResponseDto>> findAllFeedbacksByBook(
      @PathVariable("book-id") Long bookId,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser
  ) {
    PageResponse<FeedbackResponseDto> response =
        feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser);
    return ResponseEntity.ok(response);
  }

}
