package ru.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewPasswordDto {

  @NotBlank
  private String oldPassword;
  @NotBlank
  private String newPassword;
  @NotBlank
  private String newPasswordRepeated;

}
