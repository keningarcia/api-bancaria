package com.keningarcia.api_bancaria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String description;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private LocalDateTime createdAt;
}
