package org.raflab.avanturaservice.entityLayer;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adventure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String difficultyLevel;

    private double basePrice;

    @ManyToOne()
    private AdventureCategory adventureCategory;

    @OneToMany(mappedBy = "adventure")
    private List<TimeSlot> timeSlotList;

    @OneToMany(mappedBy = "adventure")
    private List<GearRequirement> gearRequirementList;

    @OneToMany(mappedBy = "adventure")
    private List<AdventureReview> adventureReviewList;
}