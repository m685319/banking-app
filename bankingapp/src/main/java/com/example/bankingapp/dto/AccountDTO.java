package com.example.bankingapp.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AccountDTO {

    private Long id;

    @Positive
    private String accountNumber;

    private String name;

    private Double balance;
}
