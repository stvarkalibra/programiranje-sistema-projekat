package org.example.finansije.client;

import org.example.finansije.config.FeignRequestInterceptorConfig;
import org.example.finansije.response.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "rezervacije",
        url = "http://localhost:8083",
        configuration = FeignRequestInterceptorConfig.class
)
public interface ReservationClient {

    @GetMapping("/api/bookings/{id}")
    BookingResponse findById(@PathVariable Long id);

    @GetMapping("/api/bookings/total-price/{id}")
    Double getTotalPrice(@PathVariable Long id);

    @PutMapping("/api/bookings/{id}")
    BookingResponse updateBookingStatus(
            @PathVariable Long id,
            @RequestBody BookingResponse bookingResponse);
}