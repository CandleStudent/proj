package ru.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
