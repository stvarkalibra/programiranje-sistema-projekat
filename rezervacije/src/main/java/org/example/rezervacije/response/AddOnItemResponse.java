package org.example.rezervacije.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOnItemResponse {
    private Long bookingId;
    private String serviceName;
    private double price;
}
