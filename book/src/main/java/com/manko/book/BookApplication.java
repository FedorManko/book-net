package com.manko.book;

import com.manko.book.entity.Role;
import com.manko.book.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BookApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookApplication.class, args);
  }

  @Bean
  public CommandLineRunner runner(RoleRepository roleRepository) {
    return args -> {
      if (roleRepository.findByName("USER").isEmpty()) {
        roleRepository.save(Role.builder().name("USER").build());
      }
    };
  }

}
