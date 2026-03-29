package org.example.rezervacije.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CancellationPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany (mappedBy = "policy")
    private List<Booking> bookings;

    private String policyName;
    private double refundPercentage;
}
