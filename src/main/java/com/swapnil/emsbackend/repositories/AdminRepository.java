package com.swapnil.emsbackend.repositories;

import java.util.List;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Account;

public interface AdminRepository {
    List<Account> findAll();

    List<Account> findAllPending();

    void makeAdmin(Integer accountId) throws InvalidRequestException;

    void declineAdmin(Integer accountId) throws InvalidRequestException;
}
