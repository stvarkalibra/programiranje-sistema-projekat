package org.example.rezervacije.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rezervacije.enums.BookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancellationResponse {
    private Long bookingId;
    private BookingStatus oldStatus;
    private BookingStatus newStatus;
    private double totalAmount;
    private double refundAmount;
    private String refundType;
    private String message;
}