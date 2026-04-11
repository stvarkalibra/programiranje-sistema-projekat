package org.example.rezervacije.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancellationPolicyResponse {
    @Schema(description = "ID politike otkazivanja", example = "1")
    private Long id;
    @Schema(description = "Naziv politike otkazivanja", example = "Full refund")
    private String policyName;
    @Schema(description = "Procenat povracaja", example = "100%")
    private double refundPercentage;
}
