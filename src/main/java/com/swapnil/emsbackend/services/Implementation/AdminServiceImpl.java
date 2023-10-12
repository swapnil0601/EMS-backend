package com.swapnil.emsbackend.services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.repositories.AdminRepository;
import com.swapnil.emsbackend.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminRepository adminRepository;

    @Override
    public void makeAdmin(Integer userId) {
        try{
            adminRepository.assignAdmin(userId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeAdmin(Integer userId) {
        try{
            adminRepository.removeAdmin(userId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
