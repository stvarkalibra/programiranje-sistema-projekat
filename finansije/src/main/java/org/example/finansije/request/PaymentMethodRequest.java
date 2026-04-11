package org.example.finansije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRequest {
    @NotNull
    private String methodName;
    @NotNull
    private boolean isActive;
}
