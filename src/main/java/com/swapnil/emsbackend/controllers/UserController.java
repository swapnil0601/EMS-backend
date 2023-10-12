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
import com.swapnil.emsbackend.models.User;
import com.swapnil.emsbackend.services.EmployeeService;
import com.swapnil.emsbackend.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    
    @Autowired
    UserService userService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> userMap){
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");

        User user = userService.registerUser(firstName, lastName, email, password, role);

        Employee employee = employeeService.addEmployee(user.getUserId());

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("userId", user.getUserId());
        userResponse.put("firstName", user.getFirstName());
        userResponse.put("lastName", user.getLastName());
        userResponse.put("email", user.getEmail());
        userResponse.put("role", user.getRole());
        userResponse.put("employeeId", employee.getEmployeeId());
        userResponse.put("token", generateJWTToken(user, employee));

        return new ResponseEntity<Map<String,Object>>(userResponse, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.validateUser(email, password);

        Employee employee = employeeService.getEmployeeById(user.getUserId());

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("userId", user.getUserId());
        userResponse.put("firstName", user.getFirstName());
        userResponse.put("lastName", user.getLastName());
        userResponse.put("email", user.getEmail());
        userResponse.put("role", user.getRole());
        userResponse.put("employeeId", employee.getEmployeeId());

        Map<String, Object> tokenMap = new HashMap<>();

        tokenMap.put("token", generateJWTToken(user, employee));

        return new ResponseEntity<Map<String,Object>>(tokenMap, HttpStatus.OK);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Map<String, Object> userMap,@PathVariable("userId") Integer userId){
        Map<String, Object> returnObj = new HashMap<>();
        String token = (String) userMap.get("token");
        Map<String, Object> claims = Constants.validateToken(token);
        if(!(Boolean) claims.get("valid")){
            returnObj.put("message", "Invalid token");
            return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.UNAUTHORIZED);
        }
        if(!userId.equals(Integer.parseInt((String) claims.get("id")))){
            returnObj.put("message", "Unauthorized Access");
            return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.UNAUTHORIZED);
        }
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");

        User user = new User(userId, firstName, lastName, email, password, role);
        User updatedUser = userService.updateUser(userId, user);

        returnObj.put("success",true);
        returnObj.put("message","User updated successfully");
        returnObj.put("user",updatedUser);

        return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.OK);
    }

    private String generateJWTToken(User user,Employee employee){
        long timestamp = System.currentTimeMillis();

        Object token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY).setIssuedAt(new Date(timestamp)).setExpiration(new Date(timestamp + 30*60*1000)).claim("id", user.getUserId()).claim("firstName", user.getFirstName()).claim("lastName", user.getLastName()).claim("email", user.getEmail()).claim("role",user.getRole()).claim("employeeid", employee.getEmployeeId()).compact();

        return token.toString();
    }
}
