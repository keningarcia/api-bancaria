package com.keningarcia.api_bancaria.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Solicitud de creación de cuenta bancaria")
public class CreateAccountRequest {

    @Schema(description = "Número de cuenta (solo dígitos, 10-20 caracteres)", example = "12345678901234567890")
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(min = 10, max = 20, message = "El número de cuenta debe tener entre 10 y 20 dígitos")
    @Pattern(regexp = "\\d+", message = "El número de cuenta debe contener solo dígitos")
    private String accountNumber;

    @Schema(description = "Moneda de la cuenta (código ISO 4217)", example = "PEN")
    @NotBlank(message = "La moneda es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda debe tener 3 caracteres (ej: PEN, USD)")
    private String currency;
}
