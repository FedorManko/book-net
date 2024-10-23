package com.manko.book.service;

import com.manko.book.dto.request.FeedbackRequestDto;
import com.manko.book.dto.request.FeedbackResponseDto;
import com.manko.book.dto.response.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {

  Long saveFeedback(FeedbackRequestDto request, Authentication connectedUser);

  PageResponse<FeedbackResponseDto> findAllFeedbacksByBook(Long bookId, int page, int size,
      Authentication connectedUser);
}
