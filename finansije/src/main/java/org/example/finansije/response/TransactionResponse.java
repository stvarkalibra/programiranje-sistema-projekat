package org.example.finansije.response;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionResponse {
    private Long invoiceId;
    private Long paymentMethodId;
    private double amount;
    private LocalDate transactionDate;
}
