package org.example.rezervacije.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.rezervacije.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Guest guest;

    @ManyToOne
    private CancellationPolicy policy;

    @OneToMany(mappedBy = "booking")
    private List<AddOnItem> addOnItems;

    @OneToMany(mappedBy = "booking")
    private List<BookingStatusLog> bookingStatusLogs;

    private Long unitId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
