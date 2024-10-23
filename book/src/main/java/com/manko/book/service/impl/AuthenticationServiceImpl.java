package com.manko.book.service.impl;

import static com.manko.book.util.ErrorCodeConstants.ROLE_WAS_NOT_INITIALIZED;

import com.manko.book.config.security.JwtService;
import com.manko.book.dto.request.AuthenticateRequestDto;
import com.manko.book.dto.request.RegistrationRequestDto;
import com.manko.book.dto.response.AuthenticateResponseDto;
import com.manko.book.dto.response.ConfirmResponseDto;
import com.manko.book.dto.response.RegistrationResponseDto;
import com.manko.book.entity.Role;
import com.manko.book.entity.Token;
import com.manko.book.entity.User;
import com.manko.book.enums.EmailTemplateName;
import com.manko.book.exception.MsBookException;
import com.manko.book.repository.RoleRepository;
import com.manko.book.repository.TokenRepositroy;
import com.manko.book.repository.UserRepository;
import com.manko.book.service.AuthenticationService;
import com.manko.book.service.EmailService;
import jakarta.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenRepositroy tokenRepositroy;
  private final EmailService emailService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Value("${application.mailing.frontend.activation-url}")
  private String activationUrl;

  @Override
  @Transactional
  public RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto)
      throws MessagingException {
    Role userRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new MsBookException(ROLE_WAS_NOT_INITIALIZED));
    User user = User.builder()
        .firstname(registrationRequestDto.getFirstname())
        .lastname(registrationRequestDto.getLastname())
        .email(registrationRequestDto.getEmail())
        .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
        .accountLocked(false)
        .enabled(false)
        .roles(List.of(userRole))
        .build();
    User savedUser = userRepository.save(user);
    sendValidationEmail(user);
    return new RegistrationResponseDto(savedUser.getId(), savedUser.getEmail());
  }

  @Override
  public AuthenticateResponseDto authenticate(AuthenticateRequestDto requestDto) {
    var auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
    var claims = new HashMap<String, Object>();
    var user = ((User) auth.getPrincipal());
    claims.put("fullName", user.getFullName());
    var jwtToken = jwtService.generateToken(claims, user);
    return new AuthenticateResponseDto(jwtToken);
  }

  @Override
  @Transactional(noRollbackFor = MsBookException.class)
  public ConfirmResponseDto confirm(String token) throws MessagingException {
    Token savedToken = tokenRepositroy.findByToken(token)
        .orElseThrow(() -> new MsBookException("Invalid token"));
    if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
      sendValidationEmail(savedToken.getUser());
      throw new MsBookException("Activation token has expired. A new token has been send");
    }
    var user = userRepository.findById(savedToken.getUser().getId())
        .orElseThrow(() -> new MsBookException("User not found"));
    user.setEnabled(true);
    userRepository.save(user);
    savedToken.setValidatedAt(LocalDateTime.now());
    tokenRepositroy.save(savedToken);
    return new ConfirmResponseDto(user.getId(), true);
  }

  private void sendValidationEmail(User user) throws MessagingException {
    String newToken = generateAndSaveActivationToken(user);
    emailService.sendEmail(user.getEmail(), user.getFullName(), EmailTemplateName.ACTIVATE_ACCOUNT,
        activationUrl,
        newToken, "Account activation");

  }

  private String generateAndSaveActivationToken(User user) {
    String generatedToken = generateActivationCode(6);
    Token token = Token.builder()
        .token(generatedToken)
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(15))
        .user(user)
        .build();
    tokenRepositroy.save(token);
    return generatedToken;
  }

  private String generateActivationCode(int length) {
    String characters = "0123456789";
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(characters.length());
      codeBuilder.append(characters.charAt(randomIndex));
    }
    return codeBuilder.toString();
  }
}
