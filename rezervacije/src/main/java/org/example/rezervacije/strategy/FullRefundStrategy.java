package org.example.rezervacije.strategy;

import org.springframework.stereotype.Component;

@Component
public class FullRefundStrategy implements RefundStrategy {

    @Override
    public double calculateRefund(double totalAmount, double refundPercentage) {
        return totalAmount;
    }

    @Override
    public String getStrategyName() {
        return "FULL_REFUND";
    }
}