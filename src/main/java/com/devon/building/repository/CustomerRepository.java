package com.devon.building.repository;

import com.devon.building.entity.Customer;
import com.devon.building.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    boolean existsByPhoneNumber(String phoneNumber);
    Customer findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndActiveTrue(String phoneNumber);
    Customer findByPhoneNumberAndActiveTrue(String phoneNumber);

    Optional<Customer> findByIdAndActiveTrue(Long id);
    List<Customer> findAllByIdInAndActiveTrue(List<Long> ids);
}
