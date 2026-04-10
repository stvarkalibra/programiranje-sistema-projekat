package org.example.rezervacije.repository;

import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.enums.BookingStatus;
import org.example.rezervacije.response.GuestBookingStatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByGuestId(Long guestId);
    boolean existsByPolicy_Id(Long policyId);

    @Query("SELECT new org.example.rezervacije.response.GuestBookingStatsResponse(" +
            "g.firstName, " +
            "g.lastName, " +
            "COUNT(b), " +
            "SUM(b.totalPrice)) " +
            "FROM Booking b " +
            "JOIN b.guest g " +
            "GROUP BY g.id, g.firstName, g.lastName " +
            "ORDER BY COUNT(b) DESC")
    List<GuestBookingStatsResponse> getGuestBookingStats();
}
