package com.keningarcia.api_bancaria.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @Builder
@Schema(description = "Información de una transacción bancaria")
public class TransactionResponse {
    @Schema(description = "ID de la transacción", example = "1")
    private Long id;

    @Schema(description = "Monto de la transacción", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Tipo de transacción (DEPOSIT, WITHDRAW, TRANSFER)", example = "DEPOSIT")
    private String type;

    @Schema(description = "Descripción de la transacción", example = "Depósito en efectivo")
    private String description;

    @Schema(description = "Número de cuenta origen", example = "12345678901234567890")
    private String sourceAccountNumber;

    @Schema(description = "Número de cuenta destino (solo para transferencias)", example = "98765432109876543210")
    private String targetAccountNumber;

    @Schema(description = "Fecha de creación de la transacción")
    private LocalDateTime createdAt;
}
