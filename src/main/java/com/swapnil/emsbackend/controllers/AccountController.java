package com.swapnil.emsbackend.controllers;

import java.util.Date;
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

import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.services.EmployeeService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.swapnil.emsbackend.services.AccountService;
import com.swapnil.emsbackend.Constants;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    
    @Autowired
    AccountService accountService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> accountMap){
        String firstName = (String) accountMap.get("firstname");
        String lastName = (String) accountMap.get("lastname");
        String email = (String) accountMap.get("email");
        String password = (String) accountMap.get("password");
        String role = (String) accountMap.get("role");

        Account account = accountService.registerAccount(firstName, lastName, email, password, role);
        
        Employee employee = employeeService.addEmployee(account.getAccountId());

        Map<String, Object> accountResponse = new HashMap<>();
        accountResponse.put("accountId", account.getAccountId());
        accountResponse.put("firstName", account.getFirstName());
        accountResponse.put("lastName", account.getLastName());
        accountResponse.put("email", account.getEmail());
        accountResponse.put("role", account.getRole());
        accountResponse.put("employeeId", employee.getEmployeeId());
        accountResponse.put("token", generateJWTToken(account, employee));

        return new ResponseEntity<Map<String,Object>>(accountResponse, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> accountMap){
        String email = (String) accountMap.get("email");
        String password = (String) accountMap.get("password");
        System.out.println(email+" "+password);
        Account account = accountService.validateAccount(email, password);
        Employee employee = employeeService.getEmployeeById(account.getAccountId());
        Map<String, Object> accountResponse = new HashMap<>();
        accountResponse.put("accountId", account.getAccountId());
        accountResponse.put("firstName", account.getFirstName());
        accountResponse.put("lastName", account.getLastName());
        accountResponse.put("email", account.getEmail());
        accountResponse.put("role", account.getRole());
        accountResponse.put("employeeId", employee.getEmployeeId());

        Map<String, Object> returnObj = new HashMap<>();

        returnObj.put("token", generateJWTToken(account, employee));
        returnObj.put("account", accountResponse);

        return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.OK);
    }

    @PostMapping("/update/{accountId}")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Map<String, Object> accountMap,@PathVariable("accountId") Integer accountId){
        Map<String, Object> returnObj = new HashMap<>();
        String token = (String) accountMap.get("token");
        Map<String,Object> tokenMap = Constants.validateToken(token);
        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (tokenMap.get("accountId") != accountId) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<>(returnObj, HttpStatus.BAD_REQUEST);
        }
        
        String firstName = (String) accountMap.get("firstName");
        String lastName = (String) accountMap.get("lastName");
        String email = (String) accountMap.get("email");
        String password = (String) accountMap.get("password");
        String role = (String) accountMap.get("role");

        Account account = new Account(accountId, firstName, lastName, email, password, role);
        Account updatedAccount = accountService.updateAccount(accountId, account);

        returnObj.put("success",true);
        returnObj.put("message","Account updated successfully");
        returnObj.put("account",updatedAccount);

        return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.OK);
    }

    private String generateJWTToken(Account account, Employee employee) {
        long timestamp = System.currentTimeMillis();
        
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Constants.API_SECRET_KEY), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + 30 * 60 * 1000))
                .claim("accountId", account.getAccountId())
                .claim("email", account.getEmail())
                .claim("firstName", account.getFirstName())
                .claim("lastName", account.getLastName())
                .claim("role", account.getRole())
                .claim("employeeId", employee.getEmployeeId())
                .compact();
        return token;
    }
}
