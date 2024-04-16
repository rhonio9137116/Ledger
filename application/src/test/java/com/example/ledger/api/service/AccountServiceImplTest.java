package com.example.ledger.api.service;

import com.example.ledger.database.entity.Account;
import com.example.ledger.database.repository.AccountRepository;
import com.example.ledger.restful.account.v1.AccountLifecycleRequest;
import com.example.ledger.restful.account.v1.AccountLifecycleResponse;
import com.example.ledger.restful.exception.GenericRestfulException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl underTest;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void accountLifecycle_good() throws GenericRestfulException {

        // given
        AccountLifecycleRequest accountLifecycleRequest = AccountLifecycleRequest.builder()
                .requestId("requestId")
                .userId(1L)
                .accountId(2L)
                .status("CLOSED").build();

        // when
        Account accountFound = new Account();
        accountFound.setId(2L);
        accountFound.setUserId(1L);
        accountFound.setAccountName("");
        accountFound.setAccountStatus(Account.STATUS_OPEN);

        when(accountRepository.findByIdAndUserId(eq(2L), eq(1L))).thenReturn(Optional.of(accountFound));

        accountFound.setAccountStatus(Account.STATUS_CLOSED);
        when(accountRepository.save(accountFound)).thenReturn(accountFound);

        AccountLifecycleResponse responseActual = underTest.accountLifecycle(accountLifecycleRequest);
        // then

        assertNotNull(responseActual);
        assertEquals("requestId", responseActual.getResponseId());
        assertEquals(1L, responseActual.getUserId());
        assertEquals(2L, responseActual.getAccountId());
        assertEquals("CLOSED", responseActual.getStatus());


    }

    @Test()
    void accountLifecycle_accountNotFound() {

        // given
        AccountLifecycleRequest accountLifecycleRequest = AccountLifecycleRequest.builder()
                .requestId("requestId")
                .userId(1L)
                .accountId(2L)
                .status("CLOSED").build();

        // when
        when(accountRepository.findByIdAndUserId(eq(2L), eq(1L))).thenReturn(Optional.empty());

        GenericRestfulException thrown = assertThrows(
                GenericRestfulException.class,
                () -> underTest.accountLifecycle(accountLifecycleRequest),
                "Expected accountLifecycle() to throw, but it didn't"
        );

        // then
        assertTrue(thrown.getMessage().contains("unable to find account with user"));


    }
}