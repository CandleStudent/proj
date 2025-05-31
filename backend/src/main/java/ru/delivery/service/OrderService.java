package ru.delivery.service;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.dictionary.PaymentType;
import ru.delivery.dto.ActiveOrderDto;
import ru.delivery.dto.MenuItemDto;
import ru.delivery.dto.NewOrderDto;
import ru.delivery.entity.Order;
import ru.delivery.entity.OrderItem;
import ru.delivery.mapper.AddressMapper;
import ru.delivery.service.crud.AddressCrudService;
import ru.delivery.service.crud.CustomerCrudService;
import ru.delivery.service.crud.MenuItemCrudService;
import ru.delivery.service.crud.OrderCrudService;
import ru.delivery.service.crud.RestaurantCrudService;

@Service
@RequiredArgsConstructor

public class OrderService {

  private final CustomerCrudService customerCrudService;
  private final AddressMapper addressMapper;
  private final AddressCrudService addressCrudService;
  private final MenuItemCrudService menuItemCrudService;
  private final OrderCrudService orderCrudService;
  private final RestaurantCrudService restaurantCrudService;


  @Transactional
  public void makeOrder(String userEmail, @Valid NewOrderDto newOrderDto) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    var address = addressCrudService.getById(newOrderDto.getCustomerAddressId());
    // todo ветвление логики для заказа на самовывоз и доставку
//    var restaurant = restaurantCrudService.getById(newOrderDto.getRestaurantId());
    // Доставка. Ресторан определяется автоматически как ближайший todo заменить заглушку на настоящий
    var restaurant = restaurantCrudService.getById(Long.parseLong("1"));

    var order = new Order()
        .setCustomer(customer)
        .setCustomerAddress(address)
        .setPaymentType(PaymentType.valueOf(newOrderDto.getPaymentType()))
        .setStatus(OrderStatus.NEW);

    BigDecimal cost = BigDecimal.ZERO;
    for (var item: newOrderDto.getMenuItems()) {

      var menuItem = menuItemCrudService.getById(item.getId());

      OrderItem orderItem = new OrderItem()
          .setOrder(order)
          .setCost(menuItem.getCost())
          .setAmount(item.getAmount())
          .setMenuItem(menuItem);

      order.addItem(orderItem);

      cost = cost.add(orderItem.getCost().multiply(BigDecimal.valueOf(orderItem.getAmount())));
    }
    order.setCost(cost);

    orderCrudService.saveOrUpdate(order);
  }

  @Transactional
  public List<ActiveOrderDto> getActiveOrders(String userEmail) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    //todo replace with orderMapper
    return customer.getOrders()
        .stream()
        .filter(order -> !order.getStatus().equals(OrderStatus.DONE))
        .map(order -> new ActiveOrderDto()
            .setId(order.getId())
            .setAddress(addressMapper.addressToAddressDto(order.getCustomerAddress()))
            .setPaymentType(order.getPaymentType().toValue())
            .setStatus(order.getStatus().toValue())
            .setCost(order.getCost())
            .setRestaurantId(order.getRestaurant().getId())
            .setMenuItems(
                order.getItems()
                    .stream()
                    .map(
                        orderItem -> new MenuItemDto()
                            .setAmount(orderItem.getAmount())
                            .setId(orderItem.getMenuItem().getId()))
                    .toList()
            ))
        .toList();
  }
}
