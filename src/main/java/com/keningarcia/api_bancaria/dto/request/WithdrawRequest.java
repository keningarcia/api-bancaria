package com.keningarcia.api_bancaria.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto mínimo es 0.01")
    @Digits(integer = 17, fraction = 2, message = "Formato de monto inválido")
    private BigDecimal amount;

    private String description;
}
