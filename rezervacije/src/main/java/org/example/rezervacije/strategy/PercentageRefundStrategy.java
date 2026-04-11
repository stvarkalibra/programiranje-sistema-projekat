package org.example.rezervacije.strategy;

import org.springframework.stereotype.Component;

@Component
public class PercentageRefundStrategy implements RefundStrategy {

    @Override
    public double calculateRefund(double totalAmount, double refundPercentage) {
        return totalAmount * refundPercentage / 100.0;
    }

    @Override
    public String getStrategyName() {
        return "PERCENTAGE_REFUND";
    }
}