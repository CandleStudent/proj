package ru.delivery.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WorkerRegisterRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;

  @NotNull
  private Long restaurantId;

  @NotBlank
  @Pattern(
      regexp = "RESTAURANT_ADMIN|COURIER",
      message = "Для регистрации на этом эндпоинте допустимы только ADMIN/COURIER")
  private String role;

  private String name;
  private String surname;
  @Size(min = 11, max = 12)
  @Pattern(regexp = "\\+?[1234567890]+")
  private String phone;
}
