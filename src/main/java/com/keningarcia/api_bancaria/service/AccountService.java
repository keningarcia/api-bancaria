package com.keningarcia.api_bancaria.service;

import com.keningarcia.api_bancaria.dto.request.CreateAccountRequest;
import com.keningarcia.api_bancaria.dto.response.AccountResponse;
import com.keningarcia.api_bancaria.dto.response.BalanceResponse;
import com.keningarcia.api_bancaria.exception.DuplicateResourceException;
import com.keningarcia.api_bancaria.exception.ResourceNotFoundException;
import com.keningarcia.api_bancaria.model.Account;
import com.keningarcia.api_bancaria.model.User;
import com.keningarcia.api_bancaria.repository.AccountRepository;
import com.keningarcia.api_bancaria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountResponse createAccount(Long userId, CreateAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new DuplicateResourceException("El número de cuenta ya existe");
        }

        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .currency(request.getCurrency().toUpperCase())
                .user(user)
                .build();

        account = accountRepository.save(account);

        return toAccountResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUser(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(this::toAccountResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        return toAccountResponse(account);
    }

    @Transactional(readOnly = true)
    public BalanceResponse getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        return BalanceResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();
    }

    private AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
