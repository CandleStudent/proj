package ru.delivery.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  @Query(
      value = """
              select r from Restaurant r
              left join fetch r.address
              where r.id= :id""")
  Optional<Restaurant> findWithAddress(Long id);

  @Query(
      value = """
              select r from Restaurant r
              left join fetch r.address""")
  List<Restaurant> findAllWithAddress();

  @Query(
      value = """
              select r from Restaurant r
              left join fetch r.address
              left join fetch r.couriers
              where r.id= :id""")
  Optional<Restaurant> findWithAddressAndCouriers(Long id);

  @Query(
      value = """
              select r from Restaurant r
              left join fetch r.address
              left join fetch r.couriers""")
  List<Restaurant> findAllWithAddressAndCouriers();

}
