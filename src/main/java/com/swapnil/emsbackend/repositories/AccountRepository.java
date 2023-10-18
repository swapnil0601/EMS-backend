package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Account;

public interface AccountRepository {
    Account create(String firstName, String lastName, String email, String password, String role) throws AuthException;

    Account findById(Integer accountId) throws NotFoundException;

    Account findByEmailAndPassword(String email,String password) throws AuthException;

    Account findByEmail(String email) throws AuthException;

    void update(Integer accountId,Account account) throws NotFoundException;
}
