package org.example.finansije.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.finansije.enums.InvoiceStatus;

import java.util.List;

@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;

    @ManyToOne
    private TaxRate taxRate;

    private double subtotal;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToMany(mappedBy = "invoice")
    private List<Transaction> transactions;
}