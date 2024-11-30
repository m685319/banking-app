package com.example.bankingapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountDTO {

    private Long id;

    @NotNull
    @Positive
    @Size(min = 10, max = 10, message = "The account number must be exactly 10 characters long")
    private String accountNumber;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @PositiveOrZero(message = "Balance must be zero or positive")
    private Double balance;
}
