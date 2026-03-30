package org.example.finansije.converter;

import org.example.finansije.entity.Invoice;
import org.example.finansije.entity.TaxRate;
import org.example.finansije.request.InvoiceRequest;
import org.example.finansije.response.InvoiceResponse;

public class InvoiceConverter {
    public static InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setBookingId(invoice.getBookingId());
        response.setTaxRateId(invoice.getTaxRate().getTaxRateId());
        response.setSubtotal(invoice.getSubtotal());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setStatus(invoice.getStatus());
        return response;
    }

    public static Invoice toEntity(InvoiceRequest request, TaxRate taxRate) {
        Invoice invoice = new Invoice();
        invoice.setBookingId(request.getBookingId());
        invoice.setTaxRate(taxRate);
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setStatus(request.getStatus());
        return invoice;
    }
}
