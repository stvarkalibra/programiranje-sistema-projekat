package org.example.finansije.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.finansije.enums.InvoiceStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    private Long id;
    private Long bookingId;
    private Long taxRateId;
    private double subtotal;
    private double totalAmount;
    private InvoiceStatus status;
}
