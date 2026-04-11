package org.example.rezervacije.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Podaci za unos ili izmenu cancellation policy")
public class CancellationPolicyRequest {

    @NotBlank(message = "Policy name is required")
    @Schema(description = "Naziv politike otkazivanja", example = "Flexible")
    private String policyName;

    @PositiveOrZero(message = "Refund percentage must be 0 or greater")
    @Schema(description = "Procenat refundacije", example = "80")
    private double refundPercentage;
}