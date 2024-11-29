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

    /**
     * Creates a new account.
     *
     * @param accountDTO the account data to be created.
     * @return the created account as a DTO.
     */
    public AccountDTO createAccount(AccountDTO accountDTO) {
        log.debug("Creating a new account for {}", accountDTO.getName());
        Account account = accountMapper.toEntity(accountDTO);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id the ID of the account to retrieve.
     * @return the account as a DTO.
     * @throws EntityNotFoundException if no account is found with the given ID.
     */
    public AccountDTO getAccount(Long id) {
        log.debug("Fetching account by ID: {}", id);
        return accountMapper.toDTO(findAccountById(id));
    }

    /**
     * Deposits a specified amount into the account.
     *
     * @param id the ID of the account.
     * @param amount the amount to deposit.
     * @throws EntityNotFoundException if no account is found with the given ID.
     */
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

    /**
     * Withdraws a specified amount from the account.
     *
     * @param id the ID of the account.
     * @param amount the amount to withdraw.
     * @throws EntityNotFoundException if no account is found with the given ID.
     * @throws InsufficientFundsException if the account has insufficient funds.
     */
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

    /**
     * Deletes an account by its ID.
     *
     * @param id the ID of the account to delete.
     * @throws EntityNotFoundException if no account is found with the given ID.
     */
    @Transactional
    public void deleteAccount(Long id) {
        log.debug("Deleting account with ID: {}", id);
        Account account = findAccountById(id);
        accountRepository.delete(account);
    }

    /**
     * Updates an existing account.
     *
     * @param accountDTO the updated account data.
     * @return the updated account as a DTO.
     * @throws EntityNotFoundException if no account is found with the given ID.
     */
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

    /**
     * Finds an account by its ID.
     *
     * @param id the ID of the account.
     * @return the account entity.
     * @throws EntityNotFoundException if no account is found with the given ID.
     */
    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account not found with ID: {}", id);
                    return new EntityNotFoundException("Account not found with ID: " + id);
                });
    }

    /**
     * Retrieves all accounts.
     *
     * @return a list of all accounts as DTOs.
     */
    public List<AccountDTO> getAll() {
        return accountMapper.toDTO(accountRepository.findAll());
    }
}
