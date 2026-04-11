package org.example.rezervacije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rezervacije.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatusLogRequest {
    @NotNull
    private Long bookingId;
    @NotNull
    private BookingStatus oldStatus;
    @NotNull
    private BookingStatus newStatus;
    @NotNull
    private LocalDateTime changedAt;
}
