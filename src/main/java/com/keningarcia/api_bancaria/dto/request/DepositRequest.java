package com.keningarcia.api_bancaria.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Solicitud de depósito")
public class DepositRequest {

    @Schema(description = "Monto a depositar", example = "1000.50")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto mínimo es 0.01")
    @Digits(integer = 17, fraction = 2, message = "Formato de monto inválido")
    private BigDecimal amount;

    @Schema(description = "Descripción del depósito", example = "Depósito en efectivo")
    private String description;
}
