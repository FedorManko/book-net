package com.manko.book.config.cors;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
    corsConfiguration.setAllowedHeaders(List.of(ORIGIN, CONTENT_TYPE, AUTHORIZATION, ACCEPT));
    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "PUT", "PATCH"));
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsFilter(source);
  }

}
