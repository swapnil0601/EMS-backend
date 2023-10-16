package com.swapnil.emsbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.models.Department;
import com.swapnil.emsbackend.services.DepartmentService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/department")
public class DepartmentController {
    
    @Autowired
    DepartmentService departmentService;

    @GetMapping("/all")
    public ResponseEntity<Map<String,Object>> getAllDepartments(){
        Map<String,Object> returnObj = new HashMap<>();
        try{
            returnObj.put("data",departmentService.getAllDepartments());
            return new ResponseEntity<>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<Map<String,Object>> getDepartmentById(HttpServletRequest request, @PathVariable("departmentId") int departmentId){
        Map<String,Object> returnObj = new HashMap<>();
        try{
            returnObj.put("data",departmentService.getDepartmentById(departmentId));
            return new ResponseEntity<>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     
    @PostMapping("/create")
    public ResponseEntity<Map<String,Object>> createDepartment(HttpServletRequest request, @RequestBody Map<String,Object> departmentMap){
        Map<String,Object> returnObj = new HashMap<>();
        String token = (String) departmentMap.get("token");
        System.out.println(token);
        Map<String, Object> tokenMap = Constants.validateToken(token);
        System.out.println(tokenMap);
        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }

        try{
            String departmentName = (String) departmentMap.get("departmentName");
            System.out.println(departmentName);
            returnObj.put("data",departmentService.addDepartment(departmentName));
            return new ResponseEntity<Map<String, Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String, Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        @PostMapping("/update/{departmentId}")
        public ResponseEntity<Map<String,Object>> updateDepartment(HttpServletRequest request, @PathVariable("departmentId") int departmentId, @RequestBody Map<String,Object> departmentMap){
            Map<String,Object> returnObj = new HashMap<>();
            String token = (String) departmentMap.get("token");
            Map<String, Object> tokenMap = Constants.validateToken(token);

            if (tokenMap.get("valid") == (Boolean) false) {
                returnObj.put("error", "invalid token");
                return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
            } else if (!tokenMap.get("role").equals("admin")) {
                returnObj.put("error", "unauthorized access");
                return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
            }

            try{
                String departmentName = (String) departmentMap.get("departmentName");
                Department department = new Department(departmentId,departmentName);
                returnObj.put("data",departmentService.updateDepartment(departmentId,department));
                return new ResponseEntity<Map<String, Object>>(returnObj,HttpStatus.OK);
            }
            catch(Exception e){
                returnObj.put("error",e.getMessage());
                return new ResponseEntity<Map<String, Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @DeleteMapping("/{departmentId}")
        public ResponseEntity<Map<String,Object>> deleteDepartment(HttpServletRequest request, @PathVariable("departmentId") int departmentId, @RequestBody Map<String,Object> departmentMap){
            Map<String,Object> returnObj = new HashMap<>();
            String token = (String) departmentMap.get("token");
            Map<String, Object> tokenMap = Constants.validateToken(token);

            if (tokenMap.get("valid") == (Boolean) false) {
                returnObj.put("error", "invalid token");
                return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
            } else if (!tokenMap.get("role").equals("admin")) {
                returnObj.put("error", "unauthorized access");
                return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
            }

            try{
                departmentService.deleteDepartment(departmentId);
                returnObj.put("success",true);
                return new ResponseEntity<Map<String, Object>>(returnObj,HttpStatus.OK);
            }
            catch(Exception e){
                returnObj.put("error",e.getMessage());
                return new ResponseEntity<Map<String, Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
