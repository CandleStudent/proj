package ru.delivery.mapper;


import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.delivery.dto.MenuItemDto;
import ru.delivery.entity.OrderItem;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrderItemMapper {

  @Mapping(source = "menuItem.id", target = "id")
  MenuItemDto orderItemToMenuItemDto(OrderItem orderItem);

  List<MenuItemDto> orderItemsToMenuItemDtos(List<OrderItem> orderItems);
}
