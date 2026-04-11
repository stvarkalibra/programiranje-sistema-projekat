package org.example.rezervacije.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AddOnItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Booking booking;

    private String serviceName;

    private double price;
}
