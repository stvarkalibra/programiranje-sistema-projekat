package org.example.rezervacije.repository;

import org.example.rezervacije.entity.BookingStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingStatusLogRepository extends JpaRepository<BookingStatusLog, Long> {
    List<BookingStatusLog> findByBookingId(Long bookingId);
}
