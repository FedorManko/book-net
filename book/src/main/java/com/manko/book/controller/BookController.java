package com.manko.book.controller;

import com.manko.book.dto.request.BookRequestDto;
import com.manko.book.dto.response.BookBorrowedResponseDto;
import com.manko.book.dto.response.BookResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.dto.response.SaveBookResponseDto;
import com.manko.book.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

  private final BookService bookService;

  @PostMapping
  public ResponseEntity<SaveBookResponseDto> saveBook(
      @RequestBody @Valid BookRequestDto bookRequestDto,
      Authentication connectedUser) {
    SaveBookResponseDto response = bookService.saveBook(bookRequestDto, connectedUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{book-id}")
  public ResponseEntity<BookResponseDto> findBookById(@PathVariable("book-id") Long bookId) {
    BookResponseDto response = bookService.findBookById(bookId);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponse<BookResponseDto>> findAllBooks(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    PageResponse<BookResponseDto> response = bookService.findAllBooks(page, size, connectedUser);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/owner")
  public ResponseEntity<PageResponse<BookResponseDto>> findAllBooksByOwner(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    PageResponse<BookResponseDto> response = bookService.findAllBooksByOwner(page, size,
        connectedUser);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/borrowed")
  public ResponseEntity<PageResponse<BookBorrowedResponseDto>> findAllBorrowedBooks(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    PageResponse<BookBorrowedResponseDto> response = bookService.findAllBorrowedBooks(page, size,
        connectedUser);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/returned")
  public ResponseEntity<PageResponse<BookBorrowedResponseDto>> findAllReturnedBooks(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser
  ) {
    PageResponse<BookBorrowedResponseDto> response = bookService.findAllReturnedBooks(page, size,
        connectedUser);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/shareable/{book-id}")
  public ResponseEntity<Long> updateShareableStatus(@PathVariable("book-id") Long bookId,
      Authentication connectedUser) {
    Long response = bookService.updateShareableStatus(bookId, connectedUser);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/archived/{book-id}")
  public ResponseEntity<Long> updateArchivedStatus(@PathVariable("book-id") Long bookId,
      Authentication connectedUser) {
    Long response = bookService.updateArchivedStatus(bookId, connectedUser);
    return ResponseEntity.ok(response);
  }

  @PostMapping("borrow/{book-id}")
  public ResponseEntity<Long> borrowBook(@PathVariable("book-id") Long bookId,
      Authentication connectedUser) {
    Long response = bookService.borrowBook(bookId, connectedUser);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("borrow/return/{book-id}")
  public ResponseEntity<Long> returnBorrowBook(@PathVariable("book-id") Long bookId,
      Authentication connectedUser) {
    Long response = bookService.returnBorrowedBook(bookId, connectedUser);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("borrow/return/approve/{book-id}")
  public ResponseEntity<Long> approveReturnBorrowBook(@PathVariable("book-id") Long bookId,
      Authentication connectedUser) {
    Long response = bookService.approveReturnBorrowedBook(bookId, connectedUser);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadBookCoverPicture(
      @PathVariable("book-id") Long bookId,
      @Parameter()
      @RequestPart("file") MultipartFile file,
      Authentication connectedUser
  ) {
    bookService.uploadBookCoverPicture(file, connectedUser, bookId);
    return ResponseEntity.accepted().build();
  }

}
