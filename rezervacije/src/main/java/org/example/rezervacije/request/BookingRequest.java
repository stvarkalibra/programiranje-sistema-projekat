package org.example.rezervacije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rezervacije.enums.BookingStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotNull
    private Long guestId;
    @NotNull
    private Long policyId;
    @NotNull
    private Long unitId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private double totalPrice;
    @NotNull
    private BookingStatus status;
}
