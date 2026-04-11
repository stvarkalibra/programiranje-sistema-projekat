package org.example.finansije.response;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private Long guestId;
    private Long policyId;
    private Long unitId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status;
}
