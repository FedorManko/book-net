package com.manko.book.service.impl;

import com.manko.book.dto.request.FeedbackRequestDto;
import com.manko.book.dto.request.FeedbackResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.entity.Book;
import com.manko.book.entity.Feedback;
import com.manko.book.entity.User;
import com.manko.book.exception.MsBookException;
import com.manko.book.mapper.FeedbackMapper;
import com.manko.book.repository.BookRepository;
import com.manko.book.repository.FeedbackRepository;
import com.manko.book.service.FeedbackService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackServiceImpl implements FeedbackService {

  private final BookRepository bookRepository;
  private final FeedbackRepository feedbackRepository;
  private final FeedbackMapper feedbackMapper;

  @Override
  @Transactional
  public Long saveFeedback(FeedbackRequestDto request, Authentication connectedUser) {
    Book book = bookRepository.findById(request.getBookId())
        .orElseThrow(() -> new MsBookException("No book found with ID:: " + request.getBookId()));
    if (book.isArchived() || !book.isShareable()) {
      throw new MsBookException(
          "You cannot give a feedback for and archived or not shareable book");
    }
    User user = ((User) connectedUser.getPrincipal());
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new MsBookException("You cannot give feedback to your own book");
    }
    Feedback feedback = feedbackMapper.toFeedback(request, book);
    return feedbackRepository.save(feedback).getId();
  }

  @Override
  public PageResponse<FeedbackResponseDto> findAllFeedbacksByBook(Long bookId, int page, int size,
      Authentication connectedUser) {
    Pageable pageable = PageRequest.of(page, size);
    User user = ((User) connectedUser.getPrincipal());
    Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);

    return feedbackMapper.toPageResponse(feedbacks, user);
  }

}
