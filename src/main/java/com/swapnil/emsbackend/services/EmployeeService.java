package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.repositories.EmployeeRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

//    public String loginUser()
    private Map<String,String> generateJWTToken(Employee employee){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp+Constants.TOKEN_VALIDITY))
                .claim("employeeId",employee.getId())
                .claim("email",employee.getEmail())
                .compact();
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return map;
    }
    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    public void addNewEmployee(Employee employee){
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if(employeeOptional.isPresent()){
            throw new IllegalStateException("Email Taken");
        }
        String hashPassword = BCrypt.hashpw(employee.getPassword(),BCrypt.gensalt(10));
        employee.setPassword(hashPassword);
        employeeRepository.save(employee);
    }

    public Employee findByEmailAndPassword(String email,String password){
        Employee employee = employeeRepository.findEmployeeByEmail(email).orElseThrow(()-> new IllegalStateException("Email not found"));
        if(!BCrypt.checkpw(password,employee.getPassword()))
        {
            throw new IllegalStateException("Wrong Password");
        }
        return employee;
    }
    public void deleteEmployee(Long employeeId){
        boolean exists = employeeRepository.existsById(employeeId);
        if(!exists){
            throw new IllegalStateException("Employee Id doesn't exist");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Transactional
    public Employee updateEmployee(Long employeeId,String name,String email){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new IllegalStateException("Employee ID not found"));
        if(name!=null && !name.isEmpty() && !Objects.equals(employee.getName(),name)){
            employee.setName(name);
        }
        if(email!=null && !email.isEmpty() && !Objects.equals(employee.getEmail(),email)){
            employee.setEmail(email);
        }
        return employee;
    }
}
