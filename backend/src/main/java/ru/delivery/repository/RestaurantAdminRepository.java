package ru.delivery.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.RestaurantAdmin;

@Repository
public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {

  @Query(
      value = """
              select c from RestaurantAdmin c
              left join fetch c.user
              where c.user.email = :email""")
  Optional<RestaurantAdmin> findByEmail(String email);

  @Query(
      value = """
              select c from RestaurantAdmin c
              left join fetch c.user
              left join fetch c.restaurant r
              left join fetch r.orders
              where c.user.email = :email""")
  Optional<RestaurantAdmin> findByEmailWithOrders(String email);

  @Query(
      value = """
              select c from RestaurantAdmin c
              where c.restaurant.id = :id""")
  Optional<RestaurantAdmin> findByRestaurantId(Long restaurantId);

  @Query(
      value = """
              select c from RestaurantAdmin c
              left join fetch c.user
              left join fetch c.restaurant r
              left join fetch r.orders
              where c.restaurant.id = :id""")
  Optional<RestaurantAdmin> findByRestaurantIdWithOrders(Long restaurantId);

}
