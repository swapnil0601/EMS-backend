package com.swapnil.emsbackend.controllers;

import com.swapnil.emsbackend.models.Admin;
import com.swapnil.emsbackend.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> getAdmin(){
        return adminService.getAdmins();
    }

    @PostMapping
    public void registerNewAdmin(@RequestBody Admin admin){
        adminService.addNewAdmin(admin);
    }

    @DeleteMapping(path="{adminId}")
    public void deleteAdmin(@PathVariable("adminId") Long id){
        adminService.deleteAdmin(id);
    }

    @PutMapping(path = "{adminId}")
    public Admin updateAdmin(
            @PathVariable("adminId") Long adminId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ){
        return adminService.updateAdmin(adminId,name,email);
    }
}
