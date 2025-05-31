package ru.delivery.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdatedOrderDto {

  private Long id;
  private List<MenuItemDto> menuItems;

}
