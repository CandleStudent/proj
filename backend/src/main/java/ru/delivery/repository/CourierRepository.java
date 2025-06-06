package ru.delivery.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

  @Query(
      value = """
              select c from Courier c
              left join fetch c.user
              where c.user.email = :email""")
  Optional<Courier> findByEmail(String email);

  @Query(
      value = """
              select c from Courier c
              left join fetch c.user
              left join fetch c.orders
              where c.user.email = :email""")
  Optional<Courier> findByEmailWithOrders(String email);

}
