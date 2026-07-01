package com.keningarcia.api_bancaria.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Solicitud de transferencia entre cuentas")
public class TransferRequest {

    @Schema(description = "Número de cuenta de destino", example = "98765432109876543210")
    @NotBlank(message = "La cuenta de destino es obligatoria")
    private String targetAccountNumber;

    @Schema(description = "Monto a transferir", example = "250.00")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto mínimo es 0.01")
    @Digits(integer = 17, fraction = 2, message = "Formato de monto inválido")
    private BigDecimal amount;

    @Schema(description = "Descripción de la transferencia", example = "Pago de servicios")
    private String description;
}
