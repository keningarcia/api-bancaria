package com.keningarcia.api_bancaria.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @Builder
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {
    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Mensaje de error", example = "Error de validación")
    private String message;

    @Schema(description = "Lista de errores detallados")
    private List<String> errors;

    @Schema(description = "Marca de tiempo del error")
    private LocalDateTime timestamp;
}
