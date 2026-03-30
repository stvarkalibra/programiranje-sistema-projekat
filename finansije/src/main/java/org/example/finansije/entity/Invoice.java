package org.example.finansije.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.finansije.enums.InvoiceStatus;

@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookindId;

    @ManyToOne
    private TaxRate taxRate;

    private double subtotal;
    private double totalAmount;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
}
