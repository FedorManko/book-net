package com.manko.book.mapper;

import com.manko.book.dto.response.BookBorrowedResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.entity.BookTransactionHistory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookTransactionHistoryMapper {

  default BookBorrowedResponseDto toBookBookBorrowedResponseDtoDto(
      BookTransactionHistory bookTransactionHistory) {
    return BookBorrowedResponseDto.builder()
        .id(bookTransactionHistory.getBook().getId())
        .title(bookTransactionHistory.getBook().getTitle())
        .authorName(bookTransactionHistory.getBook().getAuthorName())
        .isbn(bookTransactionHistory.getBook().getIsbn())
        .rate(bookTransactionHistory.getBook().getRate())
        .returned(bookTransactionHistory.isReturned())
        .returnApproved(bookTransactionHistory.isReturnApproved())
        .build();
  }

  default PageResponse<BookBorrowedResponseDto> toPageResponse(
      Page<BookTransactionHistory> bookTransactionHistories) {
    List<BookBorrowedResponseDto> responseDtoList = bookTransactionHistories.getContent().stream()
        .map(this::toBookBookBorrowedResponseDtoDto)
        .toList();

    return PageResponse.<BookBorrowedResponseDto>builder()
        .content(responseDtoList)
        .number(bookTransactionHistories.getNumber())
        .size(bookTransactionHistories.getSize())
        .totalElements(bookTransactionHistories.getTotalElements())
        .totalPages(bookTransactionHistories.getTotalPages())
        .first(bookTransactionHistories.isFirst())
        .last(bookTransactionHistories.isLast())
        .build();
  }
}
