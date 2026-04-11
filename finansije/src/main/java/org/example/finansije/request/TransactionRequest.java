package org.example.finansije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotNull
    private Long invoiceId;
    @NotNull
    private Long paymentMethodId;
    @NotNull
    private double amount;
    @NotNull
    private LocalDate transactionDate;
}
