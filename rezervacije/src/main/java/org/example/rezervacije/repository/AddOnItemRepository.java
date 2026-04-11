package org.example.rezervacije.repository;

import org.example.rezervacije.entity.AddOnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddOnItemRepository extends JpaRepository<AddOnItem, Long> {
    List<AddOnItem> findByBooking_Id(Long bookingId);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM AddOnItem a WHERE a.booking.id = :bookingId")
    Double sumPriceByBookingId(@Param("bookingId") Long bookingId);
}
