package ru.delivery.service.crud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.Order;
import ru.delivery.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderCrudService {

  private final OrderRepository orderRepository;

  public Order saveOrUpdate(Order order) {
    return orderRepository.save(order);
  }

}
