package org.raflab.guideservice.clientLayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteAdventureDTO {
    private Long id;

    @JsonProperty("title")
    private String adventureName;

    @JsonProperty("basePrice")
    private double price;

    private String difficultyLevel;
}