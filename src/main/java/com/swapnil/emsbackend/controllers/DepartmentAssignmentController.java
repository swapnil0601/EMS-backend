package com.swapnil.emsbackend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.models.DepartmentAssignment;
import com.swapnil.emsbackend.services.DepartmentAssignmentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/assign")
public class DepartmentAssignmentController {
    
    @Autowired
    DepartmentAssignmentService departmentAssignmentService;

    @GetMapping("/getAssignmentInfo")
    public ResponseEntity<Map<String,Object>> getAssignmentInfo(HttpServletRequest request){
        Map<String,Object> returnObj = new HashMap<>();
        try{
            returnObj.put("data", departmentAssignmentService.getAssignmentInfo());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUnassignedEmployees")
    public ResponseEntity<Map<String,Object>> getUnassignedEmployees(HttpServletRequest request){
        Map<String,Object> returnObj = new HashMap<>();
        try{
            returnObj.put("data", departmentAssignmentService.getUnassignedEmployees());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assignEmployee")
    public ResponseEntity<Map<String,Object>> assignEmployeeToDepartment(HttpServletRequest request, @RequestBody Map<String,Object> departmentAssignmentMap){
        Map<String,Object> returnObj = new HashMap<>();
        System.out.println(departmentAssignmentMap);
        String token = (String) departmentAssignmentMap.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }

        try{
            Integer departmentId = (Integer) departmentAssignmentMap.get("departmentId");
            Integer employeeId = (Integer) departmentAssignmentMap.get("employeeId");
            
            // Check if the employee is already assigned to a department
            DepartmentAssignment existingAssignment = departmentAssignmentService.getDepartmentAssignmentByEmployeeId(employeeId);
            if (existingAssignment != null) {
                // Update the existing assignment with the new department and assignment date
                existingAssignment.setDepartmentId(departmentId);
                DepartmentAssignment updatedAssignment = departmentAssignmentService.updateDepartmentAssignment(existingAssignment);
                returnObj.put("data", updatedAssignment);
            } else {
                // Create a new assignment if the employee is not assigned to any department
                DepartmentAssignment newAssignment = departmentAssignmentService.addDepartmentAssignment(employeeId, departmentId);
                returnObj.put("data", newAssignment);
            }

            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/removeEmployee")
    public ResponseEntity<Map<String,Object>> removeEmployee(HttpServletRequest request, @RequestBody Map<String,Object> departmentAssignmentMap){
        Map<String,Object> returnObj = new HashMap<>();
        
        String token = (String) departmentAssignmentMap.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }

        try{
            Integer departmentId = (Integer) departmentAssignmentMap.get("departmentId");
            Integer employeeId = (Integer) departmentAssignmentMap.get("employeeId");
            departmentAssignmentService.deleteDepartmentAssignment(employeeId, departmentId);

            returnObj.put("Success", true);
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
