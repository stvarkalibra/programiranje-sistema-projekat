package org.example.finansije.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.example.finansije.client.ReservationClient;
import org.example.finansije.converter.InvoiceConverter;
import org.example.finansije.entity.Invoice;
import org.example.finansije.entity.TaxRate;
import org.example.finansije.enums.InvoiceStatus;
import org.example.finansije.repository.InvoiceRepository;
import org.example.finansije.repository.TaxRateRepository;
import org.example.finansije.request.InvoiceRequest;
import org.example.finansije.response.BookingResponse;
import org.example.finansije.response.InvoiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final TaxRateRepository taxRateRepository;

    private final ReservationClient reservationClient;

    @Retry(name = "reservationServiceRetry", fallbackMethod = "saveFallback")
    @CircuitBreaker(name = "reservationServiceCB", fallbackMethod = "saveFallback")
    public InvoiceResponse save(InvoiceRequest request) {

        validateInvoiceRequest(request);

        BookingResponse bookingResponse = getExistingBooking(request.getBookingId());

        if ("CANCELLED".equalsIgnoreCase(bookingResponse.getStatus())) {
            throw new IllegalArgumentException("Invoice cannot be created for cancelled booking.");
        }

        if (invoiceRepository.findByBookingId(request.getBookingId()).isPresent()) {
            throw new IllegalArgumentException("Invoice already exists for booking id " + request.getBookingId());
        }

        TaxRate taxRate = taxRateRepository.findById(request.getTaxRateId())
                .orElseThrow(() -> new IllegalArgumentException("TaxRate not found"));

        validateInvoiceAmounts(request, taxRate);

        Invoice invoice = InvoiceConverter.toEntity(request, taxRate);

        if (invoice.getSubtotal() == 0) {
            double subtotal = getBookingTotalPrice(request.getBookingId());
            invoice.setSubtotal(subtotal);
        }

        if (invoice.getTotalAmount() == 0) {
            double total = invoice.getSubtotal() * (1 + invoice.getTaxRate().getPercentage() / 100);
            invoice.setTotalAmount(Math.round(total * 100.0) / 100.0);
        }

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

    public InvoiceResponse findByBookingId(Long bookingId) {

        return InvoiceConverter.toResponse(invoiceRepository.findByBookingId(bookingId).orElseThrow(() -> new RuntimeException("Invoice not found")));
    }

    @Retry(name = "reservationServiceRetry", fallbackMethod = "updateFallback")
    @CircuitBreaker(name = "reservationServiceCB", fallbackMethod = "updateFallback")
    public InvoiceResponse update(Long id, InvoiceRequest request) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        validateInvoiceRequest(request);

        BookingResponse bookingResponse = getExistingBooking(request.getBookingId());

        if ("CANCELLED".equalsIgnoreCase(bookingResponse.getStatus())) {
            throw new IllegalArgumentException("Invoice cannot be updated for cancelled booking.");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalArgumentException("Paid invoice cannot be modified.");
        }

        Invoice existingForBooking = invoiceRepository.findByBookingId(request.getBookingId()).orElse(null);
        if (existingForBooking != null && !existingForBooking.getId().equals(id)) {
            throw new IllegalArgumentException("Another invoice already exists for booking id " + request.getBookingId());
        }

        TaxRate taxRate = taxRateRepository.findById(request.getTaxRateId())
                .orElseThrow(() -> new RuntimeException("TaxRate not found"));

        validateInvoiceAmounts(request, taxRate);

        invoice.setBookingId(request.getBookingId());
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

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalArgumentException("Paid invoice cannot be deleted.");
        }

        invoiceRepository.delete(invoice);
    }

    @CircuitBreaker(name = "reservationServiceCB", fallbackMethod = "updateBookingStatusFallback")
    public void updateBookingStatus(Long bookingId) {

        Invoice invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        BookingResponse bookingResponse = getExistingBooking(bookingId);

        if (bookingResponse.getStartDate() == null) {
            throw new IllegalArgumentException("Booking start date is missing.");
        }

        if (invoice.getStatus() != InvoiceStatus.PAID && !bookingResponse.getStartDate().isAfter(LocalDate.now())) {
            bookingResponse.setStatus("CANCELLED");
        }

        reservationClient.updateBookingStatus(bookingId, bookingResponse);
    }

    //Validate metode

    private void validateInvoiceRequest(InvoiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Invoice request must not be null.");
        }

        if (request.getBookingId() == null || request.getBookingId() <= 0) {
            throw new IllegalArgumentException("Booking id must be a positive number.");
        }

        if (request.getTaxRateId() == null || request.getTaxRateId() <= 0) {
            throw new IllegalArgumentException("TaxRate id must be a positive number.");
        }

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Invoice status must not be null.");
        }

        if (request.getSubtotal() < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative.");
        }

        if (request.getTotalAmount() < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative.");
        }
    }

    private void validateInvoiceAmounts(InvoiceRequest request, TaxRate taxRate) {
        if (request.getSubtotal() > 0 && request.getTotalAmount() > 0) {
            double expectedTotal = request.getSubtotal() * (1 + taxRate.getPercentage() / 100);
            expectedTotal = Math.round(expectedTotal * 100.0) / 100.0;

            if (Math.abs(request.getTotalAmount() - expectedTotal) > 0.01) {
                throw new IllegalArgumentException("Total amount is not valid. Expected value is " + expectedTotal);
            }
        }
    }

    private BookingResponse getExistingBooking(Long bookingId) {
        try {
            return reservationClient.findById(bookingId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Booking with id " + bookingId + " does not exist.");
        }
    }

    private double getBookingTotalPrice(Long bookingId) {
        try {
            return reservationClient.getTotalPrice(bookingId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Booking with id " + bookingId + " does not exist.");
        }
    }

    //Fallback metode

    private InvoiceResponse saveFallback(InvoiceRequest request, IllegalArgumentException e) {
        throw e;
    }

    private InvoiceResponse saveFallback(InvoiceRequest request, CallNotPermittedException e) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Reservation service circuit breaker is OPEN. Please try again later."
        );
    }

    private InvoiceResponse saveFallback(InvoiceRequest request, FeignException.NotFound e) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Booking with id " + request.getBookingId() + " does not exist."
        );
    }

    private InvoiceResponse saveFallback(InvoiceRequest request, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Reservation service is currently unavailable. Invoice cannot be created."
        );
    }

    private InvoiceResponse updateFallback(Long id, InvoiceRequest request, Throwable t) {
        if (t instanceof IllegalArgumentException e) {
            throw e;
        }

        if (t instanceof CallNotPermittedException) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Reservation service circuit breaker is OPEN. Please try again later."
            );
        }

        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Booking with id " + request.getBookingId() + " does not exist."
            );
        }

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Reservation service is currently unavailable. Invoice cannot be updated."
        );
    }

    private void updateBookingStatusFallback(Long bookingId, Throwable t) {
        if (t instanceof IllegalArgumentException e) {
            throw e;
        }

        if (t instanceof CallNotPermittedException) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Reservation service circuit breaker is OPEN. Booking status cannot be updated now."
            );
        }

        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Booking with id " + bookingId + " does not exist."
            );
        }

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Reservation service is currently unavailable. Booking status update failed."
        );
    }
}