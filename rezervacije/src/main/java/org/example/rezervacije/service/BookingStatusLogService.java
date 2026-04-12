package org.example.rezervacije.service;

import lombok.RequiredArgsConstructor;
import org.example.rezervacije.converters.BookingStatusLogConverter;
import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.entity.BookingStatusLog;
import org.example.rezervacije.repository.BookingRepository;
import org.example.rezervacije.repository.BookingStatusLogRepository;
import org.example.rezervacije.request.BookingStatusLogRequest;
import org.example.rezervacije.response.BookingStatusLogResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingStatusLogService {

    private final BookingStatusLogRepository bookingStatusLogRepository;
    private final BookingRepository bookingRepository;

    public BookingStatusLogResponse save(BookingStatusLogRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        BookingStatusLog statusLog = BookingStatusLogConverter.toEntity(request, booking);
        BookingStatusLog saved = bookingStatusLogRepository.save(statusLog);

        return BookingStatusLogConverter.toResponse(saved);
    }

    public List<BookingStatusLogResponse> findAll() {
        return bookingStatusLogRepository.findAll()
                .stream()
                .map(BookingStatusLogConverter::toResponse)
                .collect(Collectors.toList());
    }

    public BookingStatusLogResponse findById(Long id) {
        BookingStatusLog bookingStatusLog = bookingStatusLogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "BookingStatusLog not found"
                ));

        return BookingStatusLogConverter.toResponse(bookingStatusLog);
    }

    public List<BookingStatusLogResponse> findByBookingId(Long bookingId) {
        return bookingStatusLogRepository.findByBookingId(bookingId)
                .stream()
                .map(BookingStatusLogConverter::toResponse)
                .collect(Collectors.toList());
    }

    public BookingStatusLogResponse update(Long id, BookingStatusLogRequest request) {
        BookingStatusLog bookingStatusLog = bookingStatusLogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "BookingStatusLog not found"
                ));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        bookingStatusLog.setBooking(booking);
        bookingStatusLog.setOldStatus(request.getOldStatus());
        bookingStatusLog.setNewStatus(request.getNewStatus());
        bookingStatusLog.setChangedAt(request.getChangedAt());

        BookingStatusLog updated = bookingStatusLogRepository.save(bookingStatusLog);
        return BookingStatusLogConverter.toResponse(updated);
    }

    public void deleteById(Long id) {
        BookingStatusLog bookingStatusLog = bookingStatusLogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "BookingStatusLog not found"
                ));

        bookingStatusLogRepository.delete(bookingStatusLog);
    }
}