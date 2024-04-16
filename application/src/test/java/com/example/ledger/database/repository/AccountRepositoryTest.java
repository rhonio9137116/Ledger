package com.example.ledger.database.repository;

import com.example.ledger.database.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository underTest;

    @Test
    void findByIdAndUserId() {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setAccountName("account1");

        underTest.save(account);

        Optional<Account> optionalAccount = underTest.findByIdAndUserId(1L, 1L);

        assertNotNull(optionalAccount.get());
        assertEquals("account1",optionalAccount.get().getAccountName());


    }
}