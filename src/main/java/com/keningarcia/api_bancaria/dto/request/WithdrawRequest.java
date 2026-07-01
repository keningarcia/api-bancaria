package com.keningarcia.api_bancaria.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Solicitud de retiro")
public class WithdrawRequest {

    @Schema(description = "Monto a retirar", example = "500.00")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto mínimo es 0.01")
    @Digits(integer = 17, fraction = 2, message = "Formato de monto inválido")
    private BigDecimal amount;

    @Schema(description = "Descripción del retiro", example = "Retiro en cajero")
    private String description;
}
