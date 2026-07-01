package com.keningarcia.api_bancaria.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponse {
    @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Rol del usuario", example = "ROLE_USER")
    private String role;
}
