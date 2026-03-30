package org.example.finansije.converter;

import org.example.finansije.entity.TaxRate;
import org.example.finansije.request.TaxRateRequest;
import org.example.finansije.response.TaxRateResponse;

public class TaxRateConverter {
    public static TaxRateResponse toResponse(TaxRate taxRate) {
        TaxRateResponse taxRateResponse = new TaxRateResponse();
        taxRateResponse.setTaxName(taxRate.getTaxName());
        taxRateResponse.setPercentage(taxRate.getPercentage());
        return taxRateResponse;
    }

    public static TaxRate toEntity(TaxRateRequest request) {
        TaxRate taxRate = new TaxRate();
        taxRate.setTaxName(request.getTaxName());
        taxRate.setPercentage(request.getPercentage());
        return taxRate;
    }
}
