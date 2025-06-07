package ru.delivery.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.entity.Courier;
import ru.delivery.entity.Order;
import ru.delivery.entity.Restaurant;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByRestaurantAndStatusIn(
      Restaurant restaurant, Set<OrderStatus> status);

  List<Order> findByCourierAndStatusIn(
      Courier courier, Set<OrderStatus> status);

}
