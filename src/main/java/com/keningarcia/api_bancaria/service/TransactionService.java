package com.keningarcia.api_bancaria.service;

import com.keningarcia.api_bancaria.dto.request.DepositRequest;
import com.keningarcia.api_bancaria.dto.request.TransferRequest;
import com.keningarcia.api_bancaria.dto.request.WithdrawRequest;
import com.keningarcia.api_bancaria.dto.response.TransactionResponse;
import com.keningarcia.api_bancaria.exception.InsufficientBalanceException;
import com.keningarcia.api_bancaria.exception.ResourceNotFoundException;
import com.keningarcia.api_bancaria.model.Account;
import com.keningarcia.api_bancaria.model.Transaction;
import com.keningarcia.api_bancaria.model.TransactionType;
import com.keningarcia.api_bancaria.repository.AccountRepository;
import com.keningarcia.api_bancaria.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransactionResponse deposit(Long accountId, DepositRequest request) {
        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .description(request.getDescription())
                .targetAccount(account)
                .build();

        transaction = transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransactionResponse withdraw(Long accountId, WithdrawRequest request) {
        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Saldo insuficiente. Saldo actual: " + account.getBalance() +
                    ", monto solicitado: " + request.getAmount());
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAWAL)
                .description(request.getDescription())
                .sourceAccount(account)
                .build();

        transaction = transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransactionResponse transfer(Long sourceAccountId, TransferRequest request) {
        Account source = accountRepository.findByIdWithLock(sourceAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta origen no encontrada"));

        Account target = accountRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada"));

        if (source.getId().equals(target.getId())) {
            throw new IllegalArgumentException("No puedes transferir a la misma cuenta");
        }

        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Saldo insuficiente. Saldo actual: " + source.getBalance() +
                    ", monto solicitado: " + request.getAmount());
        }

        source.setBalance(source.getBalance().subtract(request.getAmount()));
        target.setBalance(target.getBalance().add(request.getAmount()));

        accountRepository.save(source);
        accountRepository.save(target);

        Transaction outflow = Transaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER_OUT)
                .description(request.getDescription())
                .sourceAccount(source)
                .targetAccount(target)
                .build();

        transactionRepository.save(outflow);

        return toTransactionResponse(outflow);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccount(Long accountId) {
        return transactionRepository
                .findBySourceAccountIdOrTargetAccountIdOrderByCreatedAtDesc(accountId, accountId)
                .stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    private TransactionResponse toTransactionResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType().name())
                .description(t.getDescription())
                .sourceAccountNumber(t.getSourceAccount() != null ? t.getSourceAccount().getAccountNumber() : null)
                .targetAccountNumber(t.getTargetAccount() != null ? t.getTargetAccount().getAccountNumber() : null)
                .createdAt(t.getCreatedAt())
                .build();
    }
}
