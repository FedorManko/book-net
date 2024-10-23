package com.manko.book.service;

import com.manko.book.dto.request.BookRequestDto;
import com.manko.book.dto.response.BookBorrowedResponseDto;
import com.manko.book.dto.response.BookResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.dto.response.SaveBookResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {

  SaveBookResponseDto saveBook(BookRequestDto bookRequestDto, Authentication connectedUser);

  BookResponseDto findBookById(Long bookId);

  PageResponse<BookResponseDto> findAllBooks(int page, int size, Authentication connectedUser);

  PageResponse<BookResponseDto> findAllBooksByOwner(
      int page, int size, Authentication connectedUser);

  PageResponse<BookBorrowedResponseDto> findAllBorrowedBooks(
      int page, int size, Authentication connectedUser);

  PageResponse<BookBorrowedResponseDto> findAllReturnedBooks(
      int page, int size, Authentication connectedUser);

  Long updateShareableStatus(Long bookId, Authentication connectedUser);

  Long updateArchivedStatus(Long bookId, Authentication connectedUser);

  Long borrowBook(Long bookId, Authentication connectedUser);

  Long returnBorrowedBook(Long bookId, Authentication connectedUser);

  Long approveReturnBorrowedBook(Long bookId, Authentication connectedUser);

  void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Long bookId);
}
