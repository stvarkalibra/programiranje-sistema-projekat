package org.example.rezervacije.repository;

import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByGuestId(Long guestId);
    boolean existsByPolicy_Id(Long policyId);
}
