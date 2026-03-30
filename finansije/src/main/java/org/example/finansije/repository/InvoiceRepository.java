package org.example.finansije.repository;

import org.example.finansije.entity.Invoice;
import org.example.finansije.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByBookindId(Long bookingId);
    boolean existsByTaxRate_TaxRateId(Long taxRateId);
}