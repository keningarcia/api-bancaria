package com.keningarcia.api_bancaria.controller;

import com.keningarcia.api_bancaria.dto.request.CreateAccountRequest;
import com.keningarcia.api_bancaria.dto.response.AccountResponse;
import com.keningarcia.api_bancaria.dto.response.BalanceResponse;
import com.keningarcia.api_bancaria.model.User;
import com.keningarcia.api_bancaria.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Gestión de cuentas bancarias")
@SecurityRequirement(name = "Bearer")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Crear una cuenta bancaria", description = "Crea una nueva cuenta para el usuario autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "El número de cuenta ya existe")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(user.getId(), request));
    }

    @Operation(summary = "Listar cuentas del usuario", description = "Obtiene todas las cuentas del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class))))
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.getAccountsByUser(user.getId()));
    }

    @Operation(summary = "Obtener cuenta por ID", description = "Obtiene los detalles de una cuenta específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @Operation(summary = "Consultar saldo", description = "Obtiene el saldo actual de una cuenta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Saldo obtenido",
            content = @Content(schema = @Schema(implementation = BalanceResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getBalance(accountId));
    }
}
