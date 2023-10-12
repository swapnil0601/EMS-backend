package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.models.Account;

public interface AccountService {
    Account getAccountById(Integer accountId);
    
    Account registerAccount(String firstName, String lastName, String email, String password, String role) throws AuthException;

    Account validateAccount(String email, String password) throws AuthException;

    Account updateAccount(Integer accountId, Account account) throws AuthException;
}
