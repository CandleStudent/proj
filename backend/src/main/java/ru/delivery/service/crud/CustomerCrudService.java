package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.Customer;
import ru.delivery.repository.CustomerRepository;

@RequiredArgsConstructor
@Service
public class CustomerCrudService {

  private final static String USER_WITH_EMAIL_NOT_FOUND_MSG = "Customer with email = %s not found";

  private final CustomerRepository customerRepository;

  public Customer getByEmail(String email) {
    return customerRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(
            USER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public Customer getByEmailWithAddresses(String email) {
    return customerRepository.findByEmailWithAddresses(email)
        .orElseThrow(() -> new EntityNotFoundException(
            USER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public Customer saveOrUpdate(Customer user) {
    return customerRepository.save(user);
  }

}
