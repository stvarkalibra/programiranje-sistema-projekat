package org.example.rezervacije.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rezervacije.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatusLogResponse {
    private Long bookingId;
    private BookingStatus oldStatus;
    private BookingStatus newStatus;
    private LocalDateTime changedAt;
}
