package org.example.finansije.service;

import lombok.RequiredArgsConstructor;
import org.example.finansije.converter.TaxRateConverter;
import org.example.finansije.entity.TaxRate;
import org.example.finansije.repository.TaxRateRepository;
import org.example.finansije.request.TaxRateRequest;
import org.example.finansije.response.TaxRateResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxRateService {

    private final TaxRateRepository taxRateRepository;

    public TaxRateResponse save(TaxRateRequest request) {

        if (taxRateRepository.existsByTaxName(request.getTaxName())) {
            throw new RuntimeException("TaxRate already exists");
        }

        TaxRate taxRate = TaxRateConverter.toEntity(request);

        TaxRate saved = taxRateRepository.save(taxRate);

        return TaxRateConverter.toResponse(saved);
    }

    public List<TaxRateResponse> findAll() {

        return taxRateRepository.findAll()
                .stream()
                .map(TaxRateConverter::toResponse)
                .collect(Collectors.toList());
    }

    public TaxRateResponse findById(Long id) {

        TaxRate taxRate = taxRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaxRate not found"));

        return TaxRateConverter.toResponse(taxRate);
    }

    public TaxRateResponse update(Long id, TaxRateRequest request) {

        TaxRate taxRate = taxRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaxRate not found"));

        taxRate.setTaxName(request.getTaxName());
        taxRate.setPercentage(request.getPercentage());

        TaxRate updated = taxRateRepository.save(taxRate);

        return TaxRateConverter.toResponse(updated);
    }

    public void deleteById(Long id) {

        TaxRate taxRate = taxRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaxRate not found"));

        taxRateRepository.delete(taxRate);
    }
}