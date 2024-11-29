package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.service.AccountService;
import lombok.RequiredArgsConstructor;
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
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountDTO createAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO);
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PutMapping
    public AccountDTO updateAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(accountDTO);
    }

    @PutMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestParam Double amount) {
        accountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestParam Double amount) {
        accountService.withdraw(id, amount);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}
