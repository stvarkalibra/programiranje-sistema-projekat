package org.example.finansije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.finansije.enums.InvoiceStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @NotNull
    private Long bookingId;
    @NotNull
    private Long taxRateId;
    @NotNull
    private double subtotal;
    @NotNull
    private double totalAmount;
    @NotNull
    private InvoiceStatus status;
}
