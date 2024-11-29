package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountDTO createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        log.debug("Creating account with details: {}", accountDTO);
        return accountService.createAccount(accountDTO);
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        log.debug("Fetching all accounts");
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        log.debug("Fetching account with ID: {}", id);
        return accountService.getAccount(id);
    }

    @PutMapping
    public AccountDTO updateAccount(@Valid @RequestBody AccountDTO accountDTO) {
        log.debug("Updating account: {}", accountDTO);
        return accountService.updateAccount(accountDTO);
    }

    @PutMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestParam Double amount) {
        log.debug("Depositing amount {} to account ID: {}", amount, id);
        accountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestParam Double amount) {
        log.debug("Withdrawing amount {} from account ID: {}", amount, id);
        accountService.withdraw(id, amount);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        log.debug("Deleting account with ID: {}", id);
        accountService.deleteAccount(id);
    }
}
