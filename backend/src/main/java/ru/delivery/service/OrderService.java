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
import ru.delivery.repository.CustomerAddressRepository;
import ru.delivery.repository.CustomerRepository;
import ru.delivery.repository.MenuItemRepository;
import ru.delivery.repository.OrderRepository;

@Service
@RequiredArgsConstructor

public class OrderService {

  private final CustomerRepository customerRepository;
  private final AddressMapper addressMapper;
  private final CustomerAddressRepository customerAddressRepository;
  private final MenuItemRepository menuItemRepository;
  private final OrderRepository orderRepository;


  @Transactional
  public void makeOrder(String userEmail, @Valid NewOrderDto newOrderDto) {
    var customer = customerRepository.findByEmailWithAddresses(userEmail)
        .orElseThrow(() -> new RuntimeException("Customer not found"));

    var address = customerAddressRepository.findById(newOrderDto.getCustomerAddressId())
        .orElseThrow(() -> new RuntimeException("Address not found"));

    var order = new Order()
        .setCustomer(customer)
        .setCustomerAddress(address)
        .setPaymentType(PaymentType.valueOf(newOrderDto.getPaymentType()))
        .setStatus(OrderStatus.NEW);

    BigDecimal cost = BigDecimal.ZERO;
    for (var item: newOrderDto.getMenuItems()) {

      var menuItem = menuItemRepository.findById(item.getId())
          .orElseThrow(() -> new RuntimeException("MenuItem not found"));

      OrderItem orderItem = new OrderItem()
          .setOrder(order)
          .setCost(menuItem.getCost())
          .setAmount(item.getAmount())
          .setMenuItem(menuItem);

      order.addItem(orderItem);
      cost = cost.add(orderItem.getCost().multiply(BigDecimal.valueOf(orderItem.getAmount())));
    }
    order.setCost(cost);

    orderRepository.save(order);
  }

  @Transactional
  public List<ActiveOrderDto> getActiveOrders(String userEmail) {
    var customer = customerRepository.findByEmailWithAddresses(userEmail)
        .orElseThrow(() -> new RuntimeException("Customer not found"));

    return customer.getOrders()
        .stream()
        .filter(order -> !order.getStatus().equals(OrderStatus.DONE))
        .map(order -> new ActiveOrderDto()
            .setAddress(addressMapper.addressToAddressDto(order.getCustomerAddress().getAddress()))
            .setPaymentType(order.getPaymentType().toValue())
            .setStatus(order.getStatus().toValue())
            .setCost(order.getCost())
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
