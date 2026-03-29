package org.example.rezervacije.converters;

import org.example.rezervacije.entity.BookingStatusLog;
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

    public static BookingStatusLog toEntity(BookingStatusLogResponse response) {
        BookingStatusLog bookingStatusLog = new BookingStatusLog();
        bookingStatusLog.setOldStatus(response.getOldStatus());
        bookingStatusLog.setNewStatus(response.getNewStatus());
        bookingStatusLog.setChangedAt(response.getChangedAt());
        return bookingStatusLog;
    }
}
