package ru.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WorkerActiveOrderDto {

  @NotNull
  private Long id;
  @NotNull
  private AddressDto customerAddress;
  @NotBlank
  private String paymentType;
  @NotNull
  private BigDecimal cost;
  @NotEmpty
  private List<MenuItemDto> menuItems;
  @NotBlank
  private String status;

  private String clientEmail;
  private String clientName;

  @Size(min = 11, max = 12)
  @Pattern(regexp = "\\+?[1234567890]+")
  private String clientPhone;

}
