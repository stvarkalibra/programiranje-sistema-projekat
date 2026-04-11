package org.example.finansije.repository;

import org.example.finansije.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    boolean existsByTaxName(String taxName);

}