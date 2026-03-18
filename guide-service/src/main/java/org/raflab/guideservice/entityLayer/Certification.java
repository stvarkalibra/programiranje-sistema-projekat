package org.raflab.guideservice.entityLayer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // npr. "Gorska služba", "Prva pomoć"

    private String issuingBody;

    private LocalDate expiryDate;

    @ManyToOne
    private GuideProfile guideProfile;
}