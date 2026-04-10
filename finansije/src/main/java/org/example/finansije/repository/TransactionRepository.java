package org.example.finansije.repository;

import org.example.finansije.entity.Transaction;
import org.example.finansije.response.PaymentMethodStatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByInvoice_Id(Long invoiceId);

    List<Transaction> findByPaymentMethod_Id(Long paymentMethodId);

    @Query("SELECT new org.example.finansije.response.PaymentMethodStatsResponse(" +
            "pm.methodName, " +
            "COUNT(t), " +
            "SUM(t.amount)) " +
            "FROM Transaction t " +
            "JOIN t.paymentMethod pm " +
            "GROUP BY pm.id, pm.methodName " +
            "ORDER BY SUM(t.amount) DESC")
    List<PaymentMethodStatsResponse> getPaymentMethodStatistics();

}