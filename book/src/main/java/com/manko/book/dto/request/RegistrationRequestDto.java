package com.manko.book.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {

  @NotEmpty(message = "Firstname is mandatory")
  @NotBlank(message = "Firstname is mandatory")
  private String firstname;

  @NotEmpty(message = "Lastname is mandatory")
  @NotBlank(message = "Lastname is mandatory")
  private String lastname;

  @Email(message = "Wrong email format")
  @NotEmpty(message = "Email is mandatory")
  @NotBlank(message = "Email is mandatory")
  private String email;

  @NotEmpty(message = "Password is mandatory")
  @NotBlank(message = "Password is mandatory")
  @Size(min = 8, message = "Password should be 8 characters long")
  private String password;

}
