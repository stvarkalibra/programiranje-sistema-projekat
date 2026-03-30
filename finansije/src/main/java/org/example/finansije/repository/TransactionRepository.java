package org.example.finansije.repository;

import org.example.finansije.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByInvoice_Id(Long invoiceId);

    List<Transaction> findByPaymentMethod_Id(Long paymentMethodId);

}