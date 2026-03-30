package org.example.rezervacije.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rezervacije.enums.BookingStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookindId;
    private Long guestId;
    private Long policyId;
    private Long unitId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private BookingStatus status;
}
