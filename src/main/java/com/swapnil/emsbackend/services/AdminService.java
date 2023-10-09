package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.models.Admin;
import com.swapnil.emsbackend.repositories.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> getAdmins(){
        return adminRepository.findAll();
    }

    public void addNewAdmin(Admin admin){
        Optional<Admin> adminOptional = adminRepository.findAdminByEmail(admin.getEmail());
        System.out.println(admin);
        if(adminOptional.isPresent()){
            throw new IllegalStateException("Email Taken");
        }
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long adminId){
        boolean exists = adminRepository.existsById(adminId);
        if(!exists){
            throw new IllegalStateException("Admin Id doesn't exist");
        }
        adminRepository.deleteById(adminId);
    }

    @Transactional
    public Admin updateAdmin(Long adminId,String name,String email){
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(()-> new IllegalStateException("Admin ID not found"));
        if(name!=null && !name.isEmpty() && !Objects.equals(admin.getName(),name)){
            admin.setName(name);
        }
        if(email!=null && !email.isEmpty() && !Objects.equals(admin.getEmail(),email)){
            admin.setEmail(email);
        }
        return admin;
    }
}
