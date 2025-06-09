package ru.delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewProfileDataDto {

  private String name;
  private String surname;
  @Size(min = 11, max = 12)
  @Pattern(regexp = "\\+?[1234567890]+")
  private String phone;
  @Email
  private String email;

}
