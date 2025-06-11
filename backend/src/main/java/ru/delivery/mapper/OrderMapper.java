package ru.delivery.mapper;


import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Named;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.dto.ActiveOrderDto;
import ru.delivery.dto.WorkerActiveOrderDto;
import ru.delivery.entity.Address;
import ru.delivery.entity.Order;

@Mapper(
    componentModel = ComponentModel.SPRING,
    uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

  @Mapping(source = "customerAddress", target = "address")
  @Mapping(source = "status", target = "status", qualifiedByName = "orderStatusToString")
  @Mapping(source = "paymentType", target = "paymentType", qualifiedByName = "enumToString")
  @Mapping(source = "items", target = "menuItems")
  @Mapping(source = "customer.user.email", target = "clientEmail")
  @Mapping(source = "customer.name", target = "clientName")
  @Mapping(source = "customer.phone", target = "clientPhone")
  @Mapping(source = "courier.name", target = "courierName")
  @Mapping(source = "courier.phone", target = "courierPhone")
  @Mapping(source = "rowInsertTime", target = "orderCreatedAt")
  @Mapping(source = "deliverySequence", target = "deliverySequence")
  WorkerActiveOrderDto orderToWorkerActiveOrderDto(Order order);

  List<WorkerActiveOrderDto> ordersToWorkerActiveOrderDtos(List<Order> orders);

  @Mapping(source = "customerAddress", target = "address")
  @Mapping(source = "customerAddress", target = "customerFormattedAddress", qualifiedByName = "setFormattedAddress")
  @Mapping(source = "paymentType", target = "paymentType", qualifiedByName = "enumToString")
  @Mapping(source = "items", target = "menuItems")
  @Mapping(source = "status", target = "status", qualifiedByName = "orderStatusToString")
  @Mapping(source = "order.restaurant.id", target = "restaurantId")
  @Mapping(source = "restaurant.address", target = "restaurantFormattedAddress", qualifiedByName = "setFormattedAddress")
  @Mapping(source = "rowInsertTime", target = "orderCreatedAt")
  ActiveOrderDto orderToActiveOrderDto(Order order);

  @Named("setFormattedAddress")
  static String setFormatedAddress(Address address) {
    return address.getFormattedAddress();
  }

  List<ActiveOrderDto> ordersToActiveOrderDtos(List<Order> orders);

  @Named("enumToString")
  static String enumToString(Enum<?> value) {
    return value != null ? value.name() : null;
  }

  @Named("orderStatusToString")
  static String orderStatusToString(OrderStatus value) {
    return value != null ? value.getStatusDescription() : null;
  }
}
