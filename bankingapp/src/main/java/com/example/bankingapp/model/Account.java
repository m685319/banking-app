package com.example.bankingapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double balance;

    public Account() {
        this.balance = 0.0;
    }
}

