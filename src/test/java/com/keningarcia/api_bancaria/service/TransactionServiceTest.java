package com.keningarcia.api_bancaria.service;

import com.keningarcia.api_bancaria.dto.request.DepositRequest;
import com.keningarcia.api_bancaria.dto.request.TransferRequest;
import com.keningarcia.api_bancaria.dto.request.WithdrawRequest;
import com.keningarcia.api_bancaria.dto.response.TransactionResponse;
import com.keningarcia.api_bancaria.exception.InsufficientBalanceException;
import com.keningarcia.api_bancaria.exception.ResourceNotFoundException;
import com.keningarcia.api_bancaria.model.Account;
import com.keningarcia.api_bancaria.model.TransactionType;
import com.keningarcia.api_bancaria.repository.AccountRepository;
import com.keningarcia.api_bancaria.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;
    private Account targetAccount;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .accountNumber("1234567890")
                .balance(new BigDecimal("1000.00"))
                .currency("PEN")
                .version(0L)
                .build();

        targetAccount = Account.builder()
                .id(2L)
                .accountNumber("0987654321")
                .balance(new BigDecimal("500.00"))
                .currency("PEN")
                .version(0L)
                .build();
    }

    @Test
    void deposit_ShouldIncreaseBalance() {
        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("500.00"));

        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionResponse response = transactionService.deposit(1L, request);

        assertEquals(new BigDecimal("1500.00"), account.getBalance());
        assertEquals(TransactionType.DEPOSIT.name(), response.getType());
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_ShouldDecreaseBalance() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAmount(new BigDecimal("300.00"));

        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionResponse response = transactionService.withdraw(1L, request);

        assertEquals(new BigDecimal("700.00"), account.getBalance());
        assertEquals(TransactionType.WITHDRAWAL.name(), response.getType());
    }

    @Test
    void withdraw_WithInsufficientBalance_ShouldThrowException() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAmount(new BigDecimal("2000.00"));

        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.withdraw(1L, request));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deposit_WithNonExistentAccount_ShouldThrowException() {
        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("100.00"));

        when(accountRepository.findByIdWithLock(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.deposit(99L, request));
    }

    @Test
    void transfer_ShouldMoveFundsBetweenAccounts() {
        TransferRequest request = new TransferRequest();
        request.setTargetAccountNumber("0987654321");
        request.setAmount(new BigDecimal("200.00"));

        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(targetAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionResponse response = transactionService.transfer(1L, request);

        assertEquals(new BigDecimal("800.00"), account.getBalance());
        assertEquals(TransactionType.TRANSFER_OUT.name(), response.getType());
    }

    @Test
    void transfer_WithInsufficientBalance_ShouldThrowException() {
        TransferRequest request = new TransferRequest();
        request.setTargetAccountNumber("0987654321");
        request.setAmount(new BigDecimal("5000.00"));

        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(targetAccount));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.transfer(1L, request));
    }

    @Test
    void transfer_ToSameAccount_ShouldThrowException() {
        TransferRequest request = new TransferRequest();
        request.setTargetAccountNumber("1234567890");
        request.setAmount(new BigDecimal("100.00"));

        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(1L, request));
    }
}
