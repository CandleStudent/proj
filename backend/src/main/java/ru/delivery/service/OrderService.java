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
import ru.delivery.entity.Address;
import ru.delivery.entity.Customer;
import ru.delivery.entity.Order;
import ru.delivery.entity.OrderItem;
import ru.delivery.entity.Restaurant;
import ru.delivery.exception.BusinessLogicException;
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
    var restaurant = chooseRestaurantForOrder(address);

    var order = new Order();
    order = setOrderDtoDataToOrder(order, newOrderDto, customer, address, restaurant);

    orderCrudService.saveOrUpdate(order);
  }

  private Restaurant chooseRestaurantForOrder(Address address) {
    //todo instead of mock do real logic
    return restaurantCrudService.getById(Long.parseLong("1"));
  }

  private Order setOrderDtoDataToOrder(
      Order order,
      NewOrderDto newOrderDto,
      Customer customer,
      Address address,
      Restaurant restaurant) {

    order.setCustomer(customer)
        .setCustomerAddress(address)
        .setPaymentType(PaymentType.valueOf(newOrderDto.getPaymentType()))
        .setStatus(OrderStatus.NEW)
        .setRestaurant(restaurant);

    BigDecimal cost = BigDecimal.ZERO;
    for (var item : newOrderDto.getMenuItems()) {

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

    return order;
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
            .setRestaurantFormattedAddress(
                order.getRestaurant().getAddress().getFormattedAddress())
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

  @Transactional
  public void deleteOrder(String userEmail, Long id) {
    var customer = customerCrudService.getByEmailWithOrders(userEmail);

    var deletingOrder = customer.getOrders().stream()
        .filter(order -> order.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException(
            "Вы пытаетесь удалить заказ, который не делали"));
    customer.getOrders().remove(deletingOrder);

    customerCrudService.saveOrUpdate(customer);
  }

}
