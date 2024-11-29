package com.example.bankingapp.mapper;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(componentModel = "spring", nullValueCheckStrategy = ALWAYS)
public interface AccountMapper {

    Account toEntity(AccountDTO dto);

    AccountDTO toDTO(Account entity);

    List<AccountDTO> toDTO(List<Account> entities);
}
