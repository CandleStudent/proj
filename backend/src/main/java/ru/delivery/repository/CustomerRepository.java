package ru.delivery.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.Customer;
import ru.delivery.entity.User;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//  Optional<User> findByEmail(String email);

}
