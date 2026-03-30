package org.example.finansije.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodName;
    private boolean isActive;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Transaction> transactions;
}
