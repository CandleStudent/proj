package ru.delivery.service.crud;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.entity.Order;
import ru.delivery.entity.Restaurant;
import ru.delivery.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderCrudService {

  private final static Set<OrderStatus> ACTIVE_STATUSES =
      Set.of(OrderStatus.NEW, OrderStatus.COOKING, OrderStatus.PACKING, OrderStatus.IN_DELIVERY);

  private final OrderRepository orderRepository;

  public List<Order> findByRestaurantAndStatusIn(Restaurant restaurant) {
    return orderRepository.findByRestaurantAndStatusIn(restaurant, ACTIVE_STATUSES);
  }

  public Order saveOrUpdate(Order order) {
    return orderRepository.save(order);
  }

}
