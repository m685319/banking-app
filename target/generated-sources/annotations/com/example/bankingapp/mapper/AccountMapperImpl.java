package com.example.bankingapp.mapper;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.model.Account;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-29T23:51:56+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Account account = new Account();

        if ( dto.getId() != null ) {
            account.setId( dto.getId() );
        }
        if ( dto.getAccountNumber() != null ) {
            account.setAccountNumber( dto.getAccountNumber() );
        }
        if ( dto.getName() != null ) {
            account.setName( dto.getName() );
        }
        if ( dto.getBalance() != null ) {
            account.setBalance( dto.getBalance() );
        }

        return account;
    }

    @Override
    public AccountDTO toDTO(Account entity) {
        if ( entity == null ) {
            return null;
        }

        AccountDTO accountDTO = new AccountDTO();

        if ( entity.getId() != null ) {
            accountDTO.setId( entity.getId() );
        }
        if ( entity.getAccountNumber() != null ) {
            accountDTO.setAccountNumber( entity.getAccountNumber() );
        }
        if ( entity.getName() != null ) {
            accountDTO.setName( entity.getName() );
        }
        if ( entity.getBalance() != null ) {
            accountDTO.setBalance( entity.getBalance() );
        }

        return accountDTO;
    }

    @Override
    public List<AccountDTO> toDTO(List<Account> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AccountDTO> list = new ArrayList<AccountDTO>( entities.size() );
        for ( Account account : entities ) {
            list.add( toDTO( account ) );
        }

        return list;
    }
}
