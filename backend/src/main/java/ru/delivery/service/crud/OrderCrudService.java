package ru.delivery.service.crud;

import static ru.delivery.constants.YummyConstant.OrderConstant.ACTIVE_STATUSES;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.entity.Courier;
import ru.delivery.entity.Order;
import ru.delivery.entity.Restaurant;
import ru.delivery.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderCrudService {

  private static final Set<OrderStatus> PACKING_STATUS_SET = Set.of(OrderStatus.PACKING);
  private final OrderRepository orderRepository;

  public List<Order> findByRestaurantAndStatusInActive(Restaurant restaurant) {
    return orderRepository.findByRestaurantAndStatusIn(restaurant, ACTIVE_STATUSES);
  }

  public List<Order> findByRestaurantAndStatusInPacking(Restaurant restaurant) {
    return orderRepository.findByRestaurantAndStatusIn(restaurant, PACKING_STATUS_SET);
  }

  public List<Order> findByCourierAndStatusIn(Courier courier) {
    return orderRepository.findByCourierAndStatusIn(courier, ACTIVE_STATUSES);
  }

  public Order saveOrUpdate(Order order) {
    return orderRepository.save(order);
  }

  public void delete(Order order) {
    orderRepository.delete(order);
  }

}
