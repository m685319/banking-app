package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.exception.InsufficientFundsException;
import com.example.bankingapp.mapper.AccountMapper;
import com.example.bankingapp.model.Account;
import com.example.bankingapp.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDTO createAccount(AccountDTO accountDTO) {
        log.debug("Creating a new account for {}", accountDTO.getName());
        Account account = accountMapper.toEntity(accountDTO);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    public AccountDTO getAccount(Long id) {
        log.debug("Fetching account by ID: {}", id);
        return accountMapper.toDTO(findAccountById(id));
    }

    @Transactional
    public void deposit(Long id, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        log.debug("Depositing {} into account {}", amount, id);
        Account account = findAccountById(id);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(Long id, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        log.debug("Withdrawing {} from account {}", amount, id);
        Account account = findAccountById(id);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        log.debug("Deleting account with ID: {}", id);
        Account account = findAccountById(id);
        accountRepository.delete(account);
    }

    @Transactional
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        log.debug("Updating account with ID: {} to new values: {}", accountDTO.getId(), accountDTO);
        Account persistedAccount = findAccountById(accountDTO.getId());
        persistedAccount.setName(accountDTO.getName());
        persistedAccount.setBalance(accountDTO.getBalance());
        persistedAccount.setAccountNumber(accountDTO.getAccountNumber());
        persistedAccount = accountRepository.save(persistedAccount);
        return accountMapper.toDTO(persistedAccount);
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account not found with ID: {}", id);
                    return new EntityNotFoundException("Account not found with ID: " + id);
                });
    }

    public List<AccountDTO> getAll() {
        return accountMapper.toDTO(accountRepository.findAll());
    }
}
