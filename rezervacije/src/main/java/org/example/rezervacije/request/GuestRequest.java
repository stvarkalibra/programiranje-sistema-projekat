package org.example.rezervacije.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Podaci za kreiranje ili izmenu gosta")
public class GuestRequest {

    @NotBlank
    @Schema(description = "Ime gosta", example = "Petar")
    private String firstName;

    @NotBlank
    @Schema(description = "Prezime gosta", example = "Petrovic")
    private String lastName;

    @NotBlank
    @Email
    @Schema(description = "Email gosta", example = "petar@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Broj telefona gosta", example = "0641234567")
    private String phoneNumber;
}