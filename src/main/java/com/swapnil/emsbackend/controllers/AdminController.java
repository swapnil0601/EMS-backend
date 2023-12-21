package com.swapnil.emsbackend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.services.AdminService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll(HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        try{
            response.put("data", adminService.getAll());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getAllPending(HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        try{
            response.put("data", adminService.getAllPending());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> makeAdmin(@RequestBody Map<String,Object> requestMap, HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        System.out.println(requestMap);
        Integer accountId = (Integer) requestMap.get("accountId");
        try{
            adminService.makeAdmin(accountId);
            response.put("message", "Admin request accepted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/decline")
    public ResponseEntity<Map<String, Object>> declineAdmin(@RequestBody Map<String,Object> requestMap, HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        System.out.println(requestMap);
        Integer accountId = (Integer) requestMap.get("accountId");
        try{
            adminService.declineAdmin(accountId);
            response.put("message", "Admin request declined");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
