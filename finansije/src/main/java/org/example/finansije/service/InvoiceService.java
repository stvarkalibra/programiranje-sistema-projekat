package org.example.finansije.service;

import lombok.RequiredArgsConstructor;
import org.example.finansije.converter.InvoiceConverter;
import org.example.finansije.entity.Invoice;
import org.example.finansije.entity.TaxRate;
import org.example.finansije.enums.InvoiceStatus;
import org.example.finansije.repository.InvoiceRepository;
import org.example.finansije.repository.TaxRateRepository;
import org.example.finansije.request.InvoiceRequest;
import org.example.finansije.response.InvoiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final TaxRateRepository taxRateRepository;

    public InvoiceResponse save(InvoiceRequest request) {

        TaxRate taxRate = taxRateRepository.findById(request.getTaxRateId())
                .orElseThrow(() -> new RuntimeException("TaxRate not found"));

        Invoice invoice = InvoiceConverter.toEntity(request);
        invoice.setTaxRate(taxRate);

        Invoice saved = invoiceRepository.save(invoice);

        return InvoiceConverter.toResponse(saved);
    }

    public List<InvoiceResponse> findAll() {

        return invoiceRepository.findAll()
                .stream()
                .map(InvoiceConverter::toResponse)
                .collect(Collectors.toList());
    }

    public InvoiceResponse findById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return InvoiceConverter.toResponse(invoice);
    }

    public List<InvoiceResponse> findByStatus(InvoiceStatus status) {

        return invoiceRepository.findByStatus(status)
                .stream()
                .map(InvoiceConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<InvoiceResponse> findByBookingId(Long bookingId) {

        return invoiceRepository.findByBookindId(bookingId)
                .stream()
                .map(InvoiceConverter::toResponse)
                .collect(Collectors.toList());
    }

    public InvoiceResponse update(Long id, InvoiceRequest request) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        TaxRate taxRate = taxRateRepository.findById(request.getTaxRateId())
                .orElseThrow(() -> new RuntimeException("TaxRate not found"));

        invoice.setBookindId(request.getBookingId());
        invoice.setTaxRate(taxRate);
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setStatus(request.getStatus());

        Invoice updated = invoiceRepository.save(invoice);

        return InvoiceConverter.toResponse(updated);
    }

    public void deleteById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoiceRepository.delete(invoice);
    }
}