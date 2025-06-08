package ru.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CourierInfoDto {

  @NotBlank
  private String name;
  @NotBlank
  private String surname;
  @NotBlank
  @Size(min = 11, max = 12)
  @Pattern(regexp = "\\+?[1234567890]+")
  private String phone;
  @NotBlank
  @Pattern(regexp = "READY|IN_DELIVERY|BUSY|INACTIVE")
  private String status;

}
