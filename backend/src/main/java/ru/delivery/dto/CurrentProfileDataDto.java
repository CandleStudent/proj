package ru.delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CurrentProfileDataDto {

  private String name;
  private String surname;

  @NotBlank
  @Email
  private String email;

  private String phone;

}
