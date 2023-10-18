package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.models.Account;

public interface AccountService {
    Account getAccountById(Integer accountId);
    
    Account register(String firstName, String lastName, String email, String password, String role) throws AuthException;

    Account login(String email, String password) throws AuthException;

    Account update(Integer accountId, Account account) throws AuthException;
}
