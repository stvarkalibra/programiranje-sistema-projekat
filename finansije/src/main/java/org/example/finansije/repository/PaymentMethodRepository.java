package org.example.finansije.repository;

import org.example.finansije.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    boolean existsByMethodName(String methodName);

    List<PaymentMethod> findByIsActive(boolean isActive);

}