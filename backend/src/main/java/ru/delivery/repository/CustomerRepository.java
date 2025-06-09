package ru.delivery.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query(
      value = """
              select c from Customer c
              left join fetch c.user
              where c.user.email = :email""")
  Optional<Customer> findByEmail(String email);

  @Query(
      value = """
              select c from Customer c
              left join fetch c.user
              left join fetch c.addresses
              where c.user.email = :email""")
  Optional<Customer> findByEmailWithAddresses(String email);

  @Query(
      value = """
              select c from Customer c
              left join fetch c.user
              left join fetch c.orders
              where c.user.email = :email""")
  Optional<Customer> findByEmailWithOrders(String email);

  @Query(
      value = """
              select c from Customer c
              left join fetch c.user
              left join fetch c.orders o
              where c.user.email = :email
              and o.status IN :activeStatuses""")
  Optional<Customer> findByEmailWithActiveOrders(String email, Set<OrderStatus> activeStatuses);

}
