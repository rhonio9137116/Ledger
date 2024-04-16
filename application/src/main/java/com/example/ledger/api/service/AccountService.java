package com.example.ledger.api.service;

import com.example.ledger.restful.account.v1.AccountLifecycleRequest;
import com.example.ledger.restful.account.v1.AccountLifecycleResponse;
import com.example.ledger.restful.exception.GenericRestfulException;

public interface AccountService {
    AccountLifecycleResponse accountLifecycle(AccountLifecycleRequest request) throws GenericRestfulException;
}
