package com.manko.book.repository;

import com.manko.book.entity.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepositroy extends JpaRepository<Token, Long> {

  Optional<Token> findByToken(String token);
}
