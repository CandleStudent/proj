package ru.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.delivery.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
