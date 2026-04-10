package org.example.rezervacije.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuestBookingStatsResponse {
    private String firstName;
    private String lastName;
    private Long totalBookings;
    private Double totalSpent;
}