package com.manko.book.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.manko.book.dto.request.AuthenticateRequestDto;
import com.manko.book.dto.request.RegistrationRequestDto;
import com.manko.book.dto.response.AuthenticateResponseDto;
import com.manko.book.dto.response.ConfirmResponseDto;
import com.manko.book.dto.response.RegistrationResponseDto;
import com.manko.book.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<RegistrationResponseDto> register(
      @RequestBody @Valid RegistrationRequestDto registrationRequestDto) throws MessagingException {
    RegistrationResponseDto responseDto = authenticationService.register(registrationRequestDto);
    return ResponseEntity.status(CREATED).body(responseDto);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticateResponseDto> authenticate(
      @RequestBody @Valid AuthenticateRequestDto requestDto) {
    AuthenticateResponseDto responseDto = authenticationService.authenticate(requestDto);
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/activate-account")
  public ResponseEntity<ConfirmResponseDto> confirm(@RequestHeader String token)
      throws MessagingException {
    ConfirmResponseDto responseDto = authenticationService.confirm(token);
    return ResponseEntity.ok(responseDto);
  }

}
