package com.manko.book.service.impl;

import static com.manko.book.util.ErrorCodeConstants.BOOK_NOT_FOUND;

import com.manko.book.dto.request.BookRequestDto;
import com.manko.book.dto.response.BookBorrowedResponseDto;
import com.manko.book.dto.response.BookResponseDto;
import com.manko.book.dto.response.PageResponse;
import com.manko.book.dto.response.SaveBookResponseDto;
import com.manko.book.entity.Book;
import com.manko.book.entity.BookTransactionHistory;
import com.manko.book.entity.User;
import com.manko.book.exception.MsBookException;
import com.manko.book.mapper.BookMapper;
import com.manko.book.mapper.BookTransactionHistoryMapper;
import com.manko.book.repository.BookRepository;
import com.manko.book.repository.BookTransactionHistoryRepository;
import com.manko.book.service.BookService;
import com.manko.book.service.FileStorageService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookMapper bookMapper;
  private final BookRepository bookRepository;
  private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
  private final BookTransactionHistoryMapper bookTransactionHistoryMapper;
  private final FileStorageService fileStorageService;

  @Override
  @Transactional
  public SaveBookResponseDto saveBook(BookRequestDto bookRequestDto, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Book book = bookMapper.toBook(bookRequestDto);
    book.setOwner(user);
    Book bookToSave = bookRepository.save(book);
    return new SaveBookResponseDto(bookToSave.getId(), bookToSave.getTitle());
  }

  @Override
  public BookResponseDto findBookById(Long bookId) {
    return bookRepository.findById(bookId)
        .map(bookMapper::toBookResponseDto)
        .orElseThrow(() -> new MsBookException(BOOK_NOT_FOUND));
  }

  @Override
  public PageResponse<BookResponseDto> findAllBooks(int page, int size,
      Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Book> books = bookRepository.findAllDisplayableBook(pageable, user.getId());
    return bookMapper.toPageResponse(books);
  }

  @Override
  public PageResponse<BookResponseDto> findAllBooksByOwner(int page, int size,
      Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Book> books = bookRepository.findAllBySpec(pageable, user.getId());
    return bookMapper.toPageResponse(books);
  }

  @Override
  public PageResponse<BookBorrowedResponseDto> findAllBorrowedBooks(int page, int size,
      Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<BookTransactionHistory> allBorrowedBooks =
        bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
    return bookTransactionHistoryMapper.toPageResponse(allBorrowedBooks);
  }

  @Override
  public PageResponse<BookBorrowedResponseDto> findAllReturnedBooks(int page, int size,
      Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<BookTransactionHistory> allBorrowedBooks =
        bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
    return bookTransactionHistoryMapper.toPageResponse(allBorrowedBooks);
  }

  @Override
  @Transactional
  public Long updateShareableStatus(Long bookId, Authentication connectedUser) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new MsBookException(BOOK_NOT_FOUND));
    User user = ((User) connectedUser.getPrincipal());
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new MsBookException("You cannot update others books shareable status");
    }
    book.setShareable(!book.isShareable());
    bookRepository.save(book);
    return bookId;
  }

  @Override
  @Transactional
  public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new MsBookException("No book found with ID:: " + bookId));
    User user = ((User) connectedUser.getPrincipal());
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new MsBookException("You cannot update others books archived status");
    }
    book.setArchived(!book.isArchived());
    bookRepository.save(book);
    return bookId;
  }

  @Override
  @Transactional
  public Long borrowBook(Long bookId, Authentication connectedUser) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new MsBookException("No book found with ID:: " + bookId));
    if (book.isArchived() || !book.isShareable()) {
      throw new MsBookException(
          "The requested book cannot be borrowed since it is archived or not shareable");
    }
    User user = ((User) connectedUser.getPrincipal());
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new MsBookException("You cannot borrow your own book");
    }
    final boolean isAlreadyBorrowedByUser =
        bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
    if (isAlreadyBorrowedByUser) {
      throw new MsBookException(
          "You already borrowed this book and it is still not returned"
              + " or the return is not approved by the owner");
    }

    BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
        .user(user)
        .book(book)
        .returned(false)
        .returnApproved(false)
        .build();
    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  @Override
  @Transactional
  public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new MsBookException("No book found with ID:: " + bookId));
    if (book.isArchived() || !book.isShareable()) {
      throw new MsBookException("The requested book is archived or not shareable");
    }
    User user = ((User) connectedUser.getPrincipal());
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new MsBookException("You cannot borrow or return your own book");
    }

    BookTransactionHistory bookTransactionHistory =
        bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
            .orElseThrow(() -> new MsBookException("You did not borrow this book"));

    bookTransactionHistory.setReturned(true);
    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  @Override
  public Long approveReturnBorrowedBook(Long bookId, Authentication connectedUser) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new MsBookException("No book found with ID:: " + bookId));
    if (book.isArchived() || !book.isShareable()) {
      throw new MsBookException("The requested book is archived or not shareable");
    }
    User user = ((User) connectedUser.getPrincipal());
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new MsBookException("You cannot approve the return of a book you do not own");
    }

    BookTransactionHistory bookTransactionHistory =
        bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
        .orElseThrow(() -> new MsBookException(
            "The book is not returned yet. You cannot approve its return"));

    bookTransactionHistory.setReturnApproved(true);
    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  @Override
  @Transactional
  public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser,
      Long bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new MsBookException("No book found with ID:: " + bookId));
    User user = ((User) connectedUser.getPrincipal());
    var profilePicture = fileStorageService.saveFile(file, user.getId());
    book.setBookCover(profilePicture);
    bookRepository.save(book);
  }
}
