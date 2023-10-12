package com.swapnil.emsbackend.controllers;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.services.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/all")
    public ResponseEntity<Map<String,Object>> getAllEmployees(HttpServletRequest request)
    {
        Map<String,Object> returnObj = new HashMap<>();
        try{
            List<Employee> employeeList = employeeService.getAllEmployees();
            returnObj.put("data", employeeList) ;
            return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.OK);
        }catch (Exception e) {
            returnObj.put("error", "Internal server error.");
            return new ResponseEntity<>(returnObj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Map<String,Object>> getEmployeeById(HttpServletRequest request,@PathVariable Integer employeeId){
        Map<String,Object> returnObj = new HashMap<>();
        try{
            Employee employee = employeeService.getEmployeeById(employeeId);
            returnObj.put("data", employee);
            return new ResponseEntity<Map<String,Object>>(returnObj, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObj.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Map<String,Object>> deleteEmployeeById(HttpServletRequest request,@PathVariable Integer employeeId,@RequestBody Map<String,Object> map){
        Map<String,Object> returnObj = new HashMap<>();

        String token = (String) map.get("token");
        Map<String,Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }

        try{
            employeeService.deleteEmployee(employeeId);
            returnObj.put("success", true);
            return new ResponseEntity<>(returnObj, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObj.put("error", errorMessage);
            return new ResponseEntity<>(returnObj, HttpStatus.BAD_REQUEST);
        }
    }
}
