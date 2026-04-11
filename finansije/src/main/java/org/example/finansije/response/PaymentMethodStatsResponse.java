package org.example.finansije.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMethodStatsResponse {
    private String methodName;
    private Long totalTransactions;
    private Double totalAmount;
}