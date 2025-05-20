package ru.delivery.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MenuItemDto {

  private Long id;
  private Integer amount;

}
