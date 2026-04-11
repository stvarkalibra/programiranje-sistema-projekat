package org.example.finansije.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRateRequest {
    @NotNull
    private String taxName;
    @NotNull
    private double percentage;
}
