package org.example.rezervacije.converters;

import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.entity.CancellationPolicy;
import org.example.rezervacije.entity.Guest;
import org.example.rezervacije.request.BookingRequest;
import org.example.rezervacije.response.BookingResponse;


public class BookingConverter {

    public static BookingResponse toResponse(Booking booking) {

        BookingResponse response = new BookingResponse();
        response.setBookindId(booking.getId());
        response.setGuestId(booking.getGuest().getId());
        response.setPolicyId(booking.getPolicy().getId());
        response.setUnitId(booking.getUnitId());
        response.setStartDate(booking.getStartDate());
        response.setEndDate(booking.getEndDate());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());

        return response;
    }

    public static Booking toEntity(BookingRequest request, Guest guest, CancellationPolicy policy) {

        Booking booking = new Booking();
        booking.setUnitId(request.getUnitId());
        booking.setGuest(guest);
        booking.setPolicy(policy);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setStatus(request.getStatus());

        return booking;
    }
}