package org.example.rezervacije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOnItemRequest {
    @NotNull
    private Long bookingId;
    @NotNull
    private String serviceName;
    @NotNull
    private double price;
}
