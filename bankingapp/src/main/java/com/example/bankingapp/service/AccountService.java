package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.exception.InsufficientFundsException;
import com.example.bankingapp.mapper.AccountMapper;
import com.example.bankingapp.model.Account;
import com.example.bankingapp.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    public AccountDTO getAccount(Long id) {
        return accountMapper.toDTO(findAccountById(id));
    }

    public void deposit(Long id, Double amount) {
        Account account = findAccountById(id);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public void withdraw(Long id, Double amount) {
        Account account = findAccountById(id);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = findAccountById(id);
        accountRepository.delete(account);
    }

    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account persistedAccount = findAccountById(accountDTO.getId());
        persistedAccount.setName(accountDTO.getName());
        persistedAccount.setBalance(accountDTO.getBalance());
        persistedAccount.setAccountNumber(accountDTO.getAccountNumber());
        persistedAccount = accountRepository.save(persistedAccount);
        return accountMapper.toDTO(persistedAccount);
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
    }

    public List<AccountDTO> getAll() {
        return accountMapper.toDTO(accountRepository.findAll());
    }
}
