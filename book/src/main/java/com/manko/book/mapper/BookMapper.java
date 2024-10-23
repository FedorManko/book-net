package com.manko.book.mapper;

import com.manko.book.dto.request.BookRequestDto;
import com.manko.book.dto.response.BookResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.entity.Book;
import com.manko.book.util.FileUtils;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

  @Mapping(target = "archived", constant = "false")
  Book toBook(BookRequestDto bookRequestDto);

  @Mapping(target = "rate", source = "book", qualifiedByName = "setRate")
  @Mapping(target = "owner", source = "book", qualifiedByName = "setOwner")
  @Mapping(target = "cover", source = "book", qualifiedByName = "setCover")
  BookResponseDto toBookResponseDto(Book book);

  @Named("setRate")
  default double setRate(Book book) {

    return book.getRate();
  }

  @Named("setOwner")
  default String setOwner(Book book) {

    return book.getOwner().getFullName();
  }

  @Named("setCover")
  default byte[] setCover(Book book) {
    String fileUrl = book.getBookCover();
    return FileUtils.readFileFromLocation(fileUrl);
  }

  default PageResponse<BookResponseDto> toPageResponse(Page<Book> books) {
    List<BookResponseDto> responseDtoList = books.getContent().stream()
        .map(this::toBookResponseDto)
        .toList();

    return PageResponse.<BookResponseDto>builder()
        .content(responseDtoList)
        .number(books.getNumber())
        .size(books.getSize())
        .totalElements(books.getTotalElements())
        .totalPages(books.getTotalPages())
        .first(books.isFirst())
        .last(books.isLast())
        .build();
  }
}
