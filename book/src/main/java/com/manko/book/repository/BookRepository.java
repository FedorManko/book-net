package com.manko.book.repository;

import com.manko.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

  @Query("""
      SELECT book FROM Book book 
      WHERE book.archived = false 
      AND book.shareable = true
      AND book.owner.id != :userId
      """)
  Page<Book> findAllDisplayableBook(Pageable pageable, Long userId);

  default Page<Book> findAllBySpec(Pageable pageable, Long id) {
    Specification<Book> specification = ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("owner").get("id"), id));
    return findAll(specification, pageable);

  }
}
