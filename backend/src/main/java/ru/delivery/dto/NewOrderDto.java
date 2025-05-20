package ru.delivery.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewOrderDto {

  private Long customerAddressId;
  private String paymentType;
  private List<MenuItemDto> menuItems;

}
