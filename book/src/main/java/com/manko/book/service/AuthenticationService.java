package com.manko.book.service;

import com.manko.book.dto.request.AuthenticateRequestDto;
import com.manko.book.dto.request.RegistrationRequestDto;
import com.manko.book.dto.response.AuthenticateResponseDto;
import com.manko.book.dto.response.ConfirmResponseDto;
import com.manko.book.dto.response.RegistrationResponseDto;
import jakarta.mail.MessagingException;

public interface AuthenticationService {

  RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto)
      throws MessagingException;

  AuthenticateResponseDto authenticate(AuthenticateRequestDto requestDto);

  ConfirmResponseDto confirm(String token) throws MessagingException;
}
