package org.example.rezervacije.strategy;

import org.springframework.stereotype.Component;

@Component
public class NoRefundStrategy implements RefundStrategy {

    @Override
    public double calculateRefund(double totalAmount, double refundPercentage) {
        return 0.0;
    }

    @Override
    public String getStrategyName() {
        return "NO_REFUND";
    }
}