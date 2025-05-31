package ru.delivery.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActiveOrderDto {

  private Long id;
  private AddressDto address;
  private String customerFormattedAddress;
  private String paymentType;
  private BigDecimal cost;
  private List<MenuItemDto> menuItems;
  private String status;
  private Long restaurantId;
  private String restaurantFormattedAddress;

}
