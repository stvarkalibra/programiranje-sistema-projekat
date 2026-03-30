package org.example.rezervacije.converters;

import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.entity.BookingStatusLog;
import org.example.rezervacije.request.BookingStatusLogRequest;
import org.example.rezervacije.response.BookingStatusLogResponse;

public class BookingStatusLogConverter {


    public static BookingStatusLogResponse toResponse(BookingStatusLog bookingStatusLog) {
        BookingStatusLogResponse response = new BookingStatusLogResponse();
        response.setBookingId(bookingStatusLog.getBooking().getId());
        response.setOldStatus(bookingStatusLog.getOldStatus());
        response.setNewStatus(bookingStatusLog.getNewStatus());
        response.setChangedAt(bookingStatusLog.getChangedAt());
        return response;
    }

    public static BookingStatusLog toEntity(BookingStatusLogRequest request, Booking booking) {
        BookingStatusLog bookingStatusLog = new BookingStatusLog();
        bookingStatusLog.setBooking(booking);
        bookingStatusLog.setOldStatus(request.getOldStatus());
        bookingStatusLog.setNewStatus(request.getNewStatus());
        bookingStatusLog.setChangedAt(request.getChangedAt());
        return bookingStatusLog;
    }
}
