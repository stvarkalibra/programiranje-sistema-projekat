package org.example.finansije.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.finansije.response.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "rezervacije", url = "http://localhost:8081")
public interface ReservationClient {
    @GetMapping("/api/bookings/{id}")
    BookingResponse findById(@PathVariable Long id);

    @GetMapping("/api/bookings/total-price/{id}")
    Double getTotalPrice(@PathVariable Long id);

    @PutMapping("/api/bookings/{id}")
    BookingResponse updateBookingStatus(@PathVariable Long id, @Valid @RequestBody BookingResponse bookingResponse);
}
