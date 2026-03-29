package org.example.rezervacije.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Podaci o gostu koji se vraćaju klijentu")
public class GuestResponse {

    @Schema(description = "Ime gosta", example = "Petar")
    private String firstName;

    @Schema(description = "Prezime gosta", example = "Petrovic")
    private String lastName;

    @Schema(description = "Email gosta", example = "petar@gmail.com")
    private String email;

    @Schema(description = "Broj telefona gosta", example = "0641234567")
    private String phoneNumber;
}