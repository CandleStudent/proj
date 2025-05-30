package ru.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddressDto {

  private Long id;

  @NotBlank
  @Size(max = 50)
  @Pattern(
      regexp = "Казань",
      message = "Допустимы адреса только в г. Казань")
  private String city;

  @NotBlank
  @Size(max = 50)
  private String street;

  @NotBlank
  @Size(max = 50)
  private String building;

  @Positive
  private Integer entrance;

  @Positive
  private Integer floor;

  @Positive
  private Integer apartments;

  private String comment;

}
