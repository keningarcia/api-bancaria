package com.keningarcia.api_bancaria.controller;

import com.keningarcia.api_bancaria.dto.request.DepositRequest;
import com.keningarcia.api_bancaria.dto.request.TransferRequest;
import com.keningarcia.api_bancaria.dto.request.WithdrawRequest;
import com.keningarcia.api_bancaria.dto.response.TransactionResponse;
import com.keningarcia.api_bancaria.service.TransactionService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
@RequiredArgsConstructor
@Tag(name = "Transacciones", description = "Depósitos, retiros y transferencias entre cuentas")
@SecurityRequirement(name = "Bearer")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Realizar un depósito", description = "Deposita dinero en una cuenta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Depósito exitoso",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.deposit(accountId, request));
    }

    @Operation(summary = "Realizar un retiro", description = "Retira dinero de una cuenta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Retiro exitoso",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Saldo insuficiente o datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(accountId, request));
    }

    @Operation(summary = "Realizar una transferencia", description = "Transfiere dinero a otra cuenta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transferencia exitosa",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Saldo insuficiente o datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Cuenta origen o destino no encontrada")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @PathVariable Long accountId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(accountId, request));
    }

    @Operation(summary = "Listar transacciones", description = "Obtiene el historial de transacciones de una cuenta")
    @ApiResponse(responseCode = "200", description = "Historial de transacciones",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))))
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccount(accountId));
    }
}
