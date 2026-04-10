package org.example.rezervacije.strategy;

public interface RefundStrategy {
    double calculateRefund(double totalAmount, double refundPercentage);
    String getStrategyName();
}