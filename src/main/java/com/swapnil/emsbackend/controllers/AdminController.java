package com.swapnil.emsbackend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.services.AdminService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/makeAdmin/{accountId}")
    public ResponseEntity<Map<String, Object>> makeAdmin(HttpServletRequest request,
            @PathVariable("accountId") Integer accountId, @RequestBody Map<String, Object> map) {

        Map<String, Object> returnObject = new HashMap<>();
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
        } else if (tokenMap.get("role").equals("admin")) {
            adminService.makeAdmin(accountId);
            returnObject.put("succes", true);
        } else {
            returnObject.put("error", "unauthorized access");
        }

        return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
    }
}
