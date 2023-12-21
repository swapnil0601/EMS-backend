package com.swapnil.emsbackend.services;

import java.util.List;

import com.swapnil.emsbackend.models.Account;

public interface AdminService {
    List<Account> getAll();

    List<Account> getAllPending();

    void makeAdmin(Integer userId);

    void declineAdmin(Integer userId);
}
