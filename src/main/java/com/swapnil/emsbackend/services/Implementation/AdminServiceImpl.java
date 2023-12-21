package com.swapnil.emsbackend.services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.repositories.AdminRepository;
import com.swapnil.emsbackend.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminRepository adminRepository;


    @Override
    public List<Account> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public List<Account> getAllPending() {
        return adminRepository.findAllPending();
    }

    @Override
    public void makeAdmin(Integer accountId) {
        adminRepository.makeAdmin(accountId);
    }

    @Override
    public void declineAdmin(Integer accountId) {
        adminRepository.declineAdmin(accountId);
    }
}
