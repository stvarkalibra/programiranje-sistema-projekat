package org.example.rezervacije.factory;

import lombok.RequiredArgsConstructor;
import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.strategy.FullRefundStrategy;
import org.example.rezervacije.strategy.NoRefundStrategy;
import org.example.rezervacije.strategy.PercentageRefundStrategy;
import org.example.rezervacije.strategy.RefundStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class RefundStrategyFactory {

    private final FullRefundStrategy fullRefundStrategy;
    private final PercentageRefundStrategy percentageRefundStrategy;
    private final NoRefundStrategy noRefundStrategy;

    public RefundStrategy createRefundStrategy(Booking booking) {
        long daysBeforeStart = ChronoUnit.DAYS.between(LocalDate.now(), booking.getStartDate());

        if (daysBeforeStart > 7) {
            return fullRefundStrategy;
        } else if (daysBeforeStart >= 3) {
            return percentageRefundStrategy;
        } else {
            return noRefundStrategy;
        }
    }
}