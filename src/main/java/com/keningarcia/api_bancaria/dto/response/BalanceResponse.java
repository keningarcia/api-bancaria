package com.keningarcia.api_bancaria.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @AllArgsConstructor @Builder
@Schema(description = "Saldo de una cuenta bancaria")
public class BalanceResponse {
    @Schema(description = "ID de la cuenta", example = "1")
    private Long accountId;

    @Schema(description = "Número de cuenta", example = "12345678901234567890")
    private String accountNumber;

    @Schema(description = "Saldo actual", example = "1500.00")
    private BigDecimal balance;

    @Schema(description = "Moneda", example = "PEN")
    private String currency;
}
