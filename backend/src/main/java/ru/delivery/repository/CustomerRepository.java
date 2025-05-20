package ru.delivery.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query(
      value = """
              select c from Customer c
              left join fetch c.user
              where c.user.email = :email""")
  Optional<Customer> findByEmail(String email);

}
