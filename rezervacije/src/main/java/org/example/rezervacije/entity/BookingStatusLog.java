package org.example.rezervacije.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.rezervacije.enums.BookingStatus;

import java.time.LocalDateTime;

@Entity
@Data
public class BookingStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private BookingStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private BookingStatus newStatus;

    private LocalDateTime changedAt;
}
