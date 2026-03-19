package org.raflab.guideservice.clientLayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // [Cilj] Ako kolega doda 50 polja, tvoj servis ne puca [cite: 45, 47]
public class RemoteAdventureDTO {
    private Long id;

    @JsonProperty("title") // [Cilj] Ako on promeni 'title' u 'naslov_ture', menjaš samo ovde [cite: 39]
    private String adventureName;

    @JsonProperty("basePrice")
    private double price;

    private String difficultyLevel;
}