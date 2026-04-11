package org.example.rezervacije.service;

import lombok.RequiredArgsConstructor;
import org.example.rezervacije.converters.BookingConverter;
import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.entity.CancellationPolicy;
import org.example.rezervacije.entity.Guest;
import org.example.rezervacije.repository.BookingRepository;
import org.example.rezervacije.repository.CancellationPolicyRepository;
import org.example.rezervacije.repository.GuestRepository;
import org.example.rezervacije.request.BookingRequest;
import org.example.rezervacije.response.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final CancellationPolicyRepository policyRepository;

    public BookingResponse save(BookingRequest request) {

        Guest guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        CancellationPolicy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        Booking booking = BookingConverter.toEntity(request, guest, policy);

        Booking saved = bookingRepository.save(booking);

        return BookingConverter.toResponse(saved);
    }

    public List<BookingResponse> findAll() {

        return bookingRepository.findAll()
                .stream()
                .map(BookingConverter::toResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse findById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return BookingConverter.toResponse(booking);
    }

    public List<BookingResponse> findByStatus(org.example.rezervacije.enums.BookingStatus status) {

        return bookingRepository.findByStatus(status)
                .stream()
                .map(BookingConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> findByGuestId(Long guestId) {

        return bookingRepository.findByGuestId(guestId)
                .stream()
                .map(BookingConverter::toResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse update(Long id, BookingRequest request) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Guest guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        CancellationPolicy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        booking.setGuest(guest);
        booking.setPolicy(policy);
        booking.setUnitId(request.getUnitId());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setStatus(request.getStatus());

        Booking updated = bookingRepository.save(booking);

        return BookingConverter.toResponse(updated);
    }

    public void deleteById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        bookingRepository.delete(booking);
    }

    public double sumTotalPriceByGuestId(Long guestId) {

        return bookingRepository.findByGuestId(guestId)
                .stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }
}