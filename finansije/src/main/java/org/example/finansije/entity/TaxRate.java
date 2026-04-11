package org.example.finansije.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taxRateId;

    private String taxName;

    private double percentage;

    @OneToMany(mappedBy = "taxRate")
    private List<Invoice> invoices;
}
