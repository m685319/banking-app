package com.example.bankingapp.repository;

import com.example.bankingapp.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}

