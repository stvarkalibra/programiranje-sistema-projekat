package org.example.finansije.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRateResponse {
    private String taxName;
    private double percentage;
}
