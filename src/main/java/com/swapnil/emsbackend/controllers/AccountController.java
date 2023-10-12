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

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.services.EmployeeService;
import com.swapnil.emsbackend.services.AccountService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    
    @Autowired
    AccountService accountService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> accountMap){
        String firstName = (String) accountMap.get("firstName");
        String lastName = (String) accountMap.get("lastName");
        String email = (String) accountMap.get("email");
        String password = (String) accountMap.get("password");
        String role = (String) accountMap.get("role");

        System.out.println("Controller " + firstName);
        Account account = accountService.registerAccount(firstName, lastName, email, password, role);

        Employee employee = employeeService.addEmployee(account.getAccountId());

        Map<String, Object> accountResponse = new HashMap<>();
        accountResponse.put("accountId", account.getAccountId());
        accountResponse.put("firstName", account.getFirstName());
        accountResponse.put("lastName", account.getLastName());
        accountResponse.put("email", account.getEmail());
        accountResponse.put("role", account.getRole());
        accountResponse.put("employeeId", employee.getEmployeeId());
//        accountResponse.put("token", generateJWTToken(account, employee));

        return new ResponseEntity<Map<String,Object>>(accountResponse, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> accountMap){
        String email = (String) accountMap.get("email");
        String password = (String) accountMap.get("password");
        System.out.println(email+" "+password);
        Account account = accountService.validateAccount(email, password);
        System.out.println("Controller " + account.getAccountId());
        Employee employee = employeeService.getEmployeeById(account.getAccountId());
        System.out.println("Controller " + employee.getEmployeeId());
        Map<String, Object> accountResponse = new HashMap<>();
        accountResponse.put("accountId", account.getAccountId());
        accountResponse.put("firstName", account.getFirstName());
        accountResponse.put("lastName", account.getLastName());
        accountResponse.put("email", account.getEmail());
        accountResponse.put("role", account.getRole());
        accountResponse.put("employeeId", employee.getEmployeeId());

        Map<String, Object> returnObj = new HashMap<>();

        // returnObj.put("token", generateJWTToken(account, employee));
        returnObj.put("account", accountResponse);

        return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.OK);
    }

    @PostMapping("/update/{accountId}")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Map<String, Object> accountMap,@PathVariable("accountId") Integer accountId){
        Map<String, Object> returnObj = new HashMap<>();
        String token = (String) accountMap.get("token");
        Map<String, Object> claims = Constants.validateToken(token);
        if(!(Boolean) claims.get("valid")){
            returnObj.put("message", "Invalid token");
            return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.UNAUTHORIZED);
        }
        if(!accountId.equals(Integer.parseInt((String) claims.get("id")))){
            returnObj.put("message", "Unauthorized Access");
            return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.UNAUTHORIZED);
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

    private String generateJWTToken(Account account,Employee employee){
        long timestamp = System.currentTimeMillis();

        Object token = Jwts.builder()
        .signWith(SignatureAlgorithm.HS256,Keys.hmacShaKeyFor(Constants.API_SECRET_KEY))
        .setIssuedAt(new Date(timestamp))
        .setExpiration(new Date(timestamp + 30*60*1000))
        .claim("id", account.getAccountId())
        .claim("firstName", account.getFirstName())
        .claim("lastName", account.getLastName())
        .claim("email", account.getEmail())
        .claim("role",account.getRole())
        .claim("employeeid", employee.getEmployeeId())
        .compact();

        return token.toString();
    }
}
