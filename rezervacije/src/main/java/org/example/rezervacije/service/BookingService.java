package org.example.rezervacije.service;

import lombok.RequiredArgsConstructor;
import org.example.rezervacije.converters.BookingConverter;
import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.entity.BookingStatusLog;
import org.example.rezervacije.entity.CancellationPolicy;
import org.example.rezervacije.entity.Guest;
import org.example.rezervacije.enums.BookingStatus;
import org.example.rezervacije.factory.RefundStrategyFactory;
import org.example.rezervacije.repository.AddOnItemRepository;
import org.example.rezervacije.repository.BookingRepository;
import org.example.rezervacije.repository.BookingStatusLogRepository;
import org.example.rezervacije.repository.CancellationPolicyRepository;
import org.example.rezervacije.repository.GuestRepository;
import org.example.rezervacije.request.BookingRequest;
import org.example.rezervacije.response.BookingResponse;
import org.example.rezervacije.response.CancellationResponse;
import org.example.rezervacije.response.GuestBookingStatsResponse;
import org.example.rezervacije.strategy.RefundStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final CancellationPolicyRepository policyRepository;
    private final AddOnItemRepository addOnItemRepository;
    private final RefundStrategyFactory refundStrategyFactory;
    private final BookingStatusLogRepository bookingStatusLogRepository;

    public BookingResponse save(BookingRequest request) {
        Guest guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        CancellationPolicy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policy not found"
                ));

        Booking booking = BookingConverter.toEntity(request, guest, policy);
        Booking saved = bookingRepository.save(booking);

        BookingStatusLog log = new BookingStatusLog();
        log.setBooking(booking);
        log.setOldStatus(booking.getStatus());
        log.setNewStatus(booking.getStatus());
        log.setChangedAt(LocalDateTime.now());

        bookingStatusLogRepository.save(log);

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking with id " + id + " not found."
                ));

        return BookingConverter.toResponse(booking);
    }

    public List<BookingResponse> findByStatus(BookingStatus status) {
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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        Guest guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        CancellationPolicy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policy not found"
                ));

        BookingStatusLog log = new BookingStatusLog();
        log.setOldStatus(booking.getStatus());

        booking.setGuest(guest);
        booking.setPolicy(policy);
        booking.setUnitId(request.getUnitId());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setStatus(request.getStatus());

        Booking updated = bookingRepository.save(booking);

        log.setBooking(booking);
        log.setNewStatus(booking.getStatus());
        log.setChangedAt(LocalDateTime.now());
        bookingStatusLogRepository.save(log);

        return BookingConverter.toResponse(updated);
    }

    public void deleteById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        bookingRepository.delete(booking);
    }

    public double sumTotalPriceByGuestId(Long guestId) {
        return bookingRepository.findByGuestId(guestId)
                .stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    public double getTotalPrice(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        double bookingPrice = booking.getTotalPrice();
        double addOnItemsPrice = addOnItemRepository.sumPriceByBookingId(bookingId);

        return bookingPrice + addOnItemsPrice;
    }

    public CancellationResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking is already cancelled"
            );
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Completed booking cannot be cancelled"
            );
        }

        if (booking.getStartDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking already started and cannot be cancelled"
            );
        }

        BookingStatus oldStatus = booking.getStatus();

        double totalAmount = getTotalPrice(bookingId);
        double refundPercentage = booking.getPolicy().getRefundPercentage();

        RefundStrategy refundStrategy = refundStrategyFactory.createRefundStrategy(booking);
        double refundAmount = refundStrategy.calculateRefund(totalAmount, refundPercentage);

        booking.setStatus(BookingStatus.CANCELLED);

        BookingStatusLog log = new BookingStatusLog();
        log.setBooking(booking);
        log.setOldStatus(oldStatus);
        log.setNewStatus(BookingStatus.CANCELLED);
        log.setChangedAt(LocalDateTime.now());

        bookingStatusLogRepository.save(log);
        bookingRepository.save(booking);

        return new CancellationResponse(
                booking.getId(),
                oldStatus,
                booking.getStatus(),
                totalAmount,
                refundAmount,
                refundStrategy.getStrategyName(),
                "Booking cancelled successfully."
        );
    }

    public List<GuestBookingStatsResponse> getGuestBookingStats() {
        return bookingRepository.getGuestBookingStats();
    }
}