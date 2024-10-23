package com.manko.book.config.jpa;

import com.manko.book.config.audit.ApplicationAuditAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

  @Bean
  public AuditorAware<Long> auditorAware() {
    return new ApplicationAuditAware();
  }

}
