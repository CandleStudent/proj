package ru.delivery.service.crud;

import static ru.delivery.constants.YummyConstant.OrderConstant.ACTIVE_STATUSES;
import static ru.delivery.constants.YummyConstant.OrderConstant.IN_DELIVERY_STATUS_SET;
import static ru.delivery.constants.YummyConstant.OrderConstant.PACKING_STATUS_SET;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.Courier;
import ru.delivery.entity.Order;
import ru.delivery.entity.Restaurant;
import ru.delivery.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderCrudService {

  private final OrderRepository orderRepository;

  public List<Order> findByRestaurantAndStatusInActive(Restaurant restaurant) {
    return orderRepository.findByRestaurantAndStatusIn(restaurant, ACTIVE_STATUSES);
  }

  public List<Order> findByRestaurantAndStatusInPacking(Restaurant restaurant) {
    return orderRepository.findByRestaurantAndStatusIn(restaurant, PACKING_STATUS_SET);
  }

  public List<Order> findByCourierAndStatusIn(Courier courier) {
    return orderRepository.findByCourierAndStatusIn(courier, IN_DELIVERY_STATUS_SET);
  }

  public Order saveOrUpdate(Order order) {
    return orderRepository.save(order);
  }

  public void delete(Order order) {
    orderRepository.delete(order);
  }

}
