package com.keningarcia.api_bancaria.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @Builder
@Schema(description = "Información de una cuenta bancaria")
public class AccountResponse {
    @Schema(description = "ID de la cuenta", example = "1")
    private Long id;

    @Schema(description = "Número de cuenta", example = "12345678901234567890")
    private String accountNumber;

    @Schema(description = "Saldo actual", example = "1500.00")
    private BigDecimal balance;

    @Schema(description = "Moneda", example = "PEN")
    private String currency;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;
}
