package ru.delivery.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddressDto {

  private Long id;

  //todo set constraints after integration with front
  @DecimalMin(value = "-90.00000000", message = "Latitude must be at least -90")
  @DecimalMax(value = "90.00000000", message = "Latitude must be at most 90")
  private BigDecimal lat;

  @DecimalMin(value = "-180.00000000", message = "Longitude must be at least -180")
  @DecimalMax(value = "180.00000000", message = "Longitude must be at most 180")
  private BigDecimal lon;


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

  public String getFormattedAddress() {
    return "%s, %s, %s".formatted(city, street, building);
  }

}
