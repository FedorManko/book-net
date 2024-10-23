package com.manko.book.mapper;

import com.manko.book.dto.request.FeedbackRequestDto;
import com.manko.book.dto.request.FeedbackResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.entity.Book;
import com.manko.book.entity.Feedback;
import com.manko.book.entity.User;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {

  @Mapping(target = "book", source = "request", qualifiedByName = "setBook")
  Feedback toFeedback(FeedbackRequestDto request, @Context Book book);

  @Mapping(target = "ownFeedback", source = "feedback", qualifiedByName = "setOwnFeedback")
  FeedbackResponseDto toFeedbackResponseDto(Feedback feedback, @Context User user);

  @Named("setOwnFeedback")
  default boolean setOwnFeedback(Feedback feedback, @Context User user) {

    return Objects.equals(feedback.getCreatedBy(), user.getId());
  }

  @Named("setBook")
  default Book setBook(FeedbackRequestDto request, @Context Book book) {
    return book;
  }

  default PageResponse<FeedbackResponseDto> toPageResponse(Page<Feedback> feedbacks, User user) {
    List<FeedbackResponseDto> responseDtoList = feedbacks.getContent().stream()
        .map(feedback -> toFeedbackResponseDto(feedback, user))
        .toList();

    return PageResponse.<FeedbackResponseDto>builder()
        .content(responseDtoList)
        .number(feedbacks.getNumber())
        .size(feedbacks.getSize())
        .totalElements(feedbacks.getTotalElements())
        .totalPages(feedbacks.getTotalPages())
        .first(feedbacks.isFirst())
        .last(feedbacks.isLast())
        .build();
  }
}