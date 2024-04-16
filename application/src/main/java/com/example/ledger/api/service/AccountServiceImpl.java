package com.example.ledger.api.service;

import com.example.ledger.database.entity.Account;
import com.example.ledger.database.repository.AccountRepository;
import com.example.ledger.restful.account.v1.AccountLifecycleRequest;
import com.example.ledger.restful.account.v1.AccountLifecycleResponse;
import com.example.ledger.restful.exception.GenericRestfulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountLifecycleResponse accountLifecycle(AccountLifecycleRequest request) throws GenericRestfulException {
        Optional<Account> optionalAccount = accountRepository.findByIdAndUserId(request.getAccountId(), request.getUserId());
        if(optionalAccount.isPresent()){
            optionalAccount.get().setAccountStatus(request.getStatus());
            Account saved = accountRepository.save(optionalAccount.get());

            return AccountLifecycleResponse.builder()
                    .responseId(request.getRequestId())
                    .accountId(saved.getId())
                    .userId(saved.getUserId())
                    .status(saved.getAccountStatus())
                    .build();
        } else {
            throw new GenericRestfulException("unable to find account with user");
        }

    }
}
