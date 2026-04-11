package org.example.rezervacije.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Guest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToMany (mappedBy = "guest")
    private List<Booking> bookings;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
