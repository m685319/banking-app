package com.example.bankingapp.controller;

import com.example.bankingapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = "TRUNCATE TABLE account RESTART IDENTITY")
@SqlMergeMode(MERGE)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testCreateAccount_success() throws Exception {
        // given
        var jsonBody = """
                {
                    "accountNumber": "1234567890",
                    "name": "John Doe",
                    "balance": 100.0
                }
                """;

        // when & then
        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Sql(statements = "INSERT INTO account(account_number, name, balance) " +
            "VALUES ('1234567890', 'John Doe', 100.0), " +
            "('0987654321', 'Jane Doe', 200.0);")
    void getAllAccounts_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/account"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[*].accountNumber").value(hasItems("1234567890", "0987654321")))
                .andExpect(jsonPath("$[*].name").value(hasItems("John Doe", "Jane Doe")));
    }

    @Test
    @Sql(statements = "INSERT INTO account(account_number, name, balance) VALUES('1234567890', 'John Doe', 100.0);")
    void getAccount_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/account/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    @Sql(statements = "INSERT INTO account(account_number, name, balance) VALUES('1234567890', 'John Doe', 100.0);")
    void testUpdateAccount_success() throws Exception {
        // given
        var jsonBody = """
                {
                    "accountNumber": "0987654321"
                }
                """;

        // when & then
        mockMvc.perform(put("/api/account/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("0987654321"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    @Sql(statements = "INSERT INTO account(account_number, name, balance) VALUES('1234567890', 'John Doe', 100.0);")
    void deposit_success() throws Exception {
        // given
        var depositAmount = "50.0";

        // when
        mockMvc.perform(put("/api/account/{id}/deposit", 1)
                        .param("amount", depositAmount))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        var account = accountRepository.findById(1L).orElseThrow();
        assertEquals(150.0, account.getBalance());
    }

    @Test
    @Sql(statements = "INSERT INTO account(account_number, name, balance) VALUES('1234567890', 'John Doe', 100.0);")
    void testWithdraw_success() throws Exception {
        // given
        var withdrawAmount = "50.0";

        // when
        mockMvc.perform(put("/api/account/{id}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", withdrawAmount))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        var account = accountRepository.findById(1L).orElseThrow();
        assertEquals("1234567890", account.getAccountNumber());
        assertEquals("John Doe", account.getName());
        assertEquals(50.0, account.getBalance());
    }

    @Test
    @Sql(statements = "INSERT INTO account(account_number, name, balance) VALUES('1234567890', 'John Doe', 100.0);")
    void deleteAccount_success() throws Exception {
        // when
        mockMvc.perform(delete("/api/account/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        var accountExists = accountRepository.findById(1L).isPresent();
        assertFalse(accountExists);
    }
}