package com.swapnil.emsbackend.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapnil.emsbackend.Constants;
import com.swapnil.emsbackend.models.Record;
import com.swapnil.emsbackend.services.DepartmentAssignmentService;
import com.swapnil.emsbackend.services.RecordService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/record")
public class RecordController {
    
    @Autowired 
    RecordService recordService;

    @Autowired
    DepartmentAssignmentService departmentAssignmentService;

    @PostMapping("/addrecord")
    public ResponseEntity<Map<String,Object>> addRecord(HttpServletRequest request,
            @RequestBody Map<String, Object> map){
        Map<String,Object> returnObj = new HashMap<>();

        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);
        System.out.println(tokenMap);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }
        System.out.println(map);

        try{
            String dateString = map.get("date").toString();
            LocalDate localDate = LocalDate.parse(dateString);
            Date date = Date.valueOf(localDate);
//            System.out.println("Record Controller "+date);
            Integer employeeId = (Integer) map.get("employeeId");
            Integer departmentId = departmentAssignmentService.getDepartmentIdFromEmployeeId(employeeId);
            System.out.println("Controller " + departmentId);
            Boolean present = (Boolean) map.get("present");
            Boolean onsite = (Boolean) map.get("onSite");
            Boolean doneSyncUpCall = (Boolean) map.get("doneSyncUpCall");

            Record newRecord = recordService.addRecord(employeeId, departmentId, date, present, onsite, doneSyncUpCall);
            System.out.println("Controller Record Created");
            returnObj.put("data", newRecord);
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/adddefaultrecord")
    public ResponseEntity<Map<String,Object>> addDefaultRecord(HttpServletRequest request, @RequestBody Map<String, Object> map){
        Map<String,Object> returnObj = new HashMap<>();

        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }
        // Date date = (Date) map.get(tokenMap.get("date"));
        Date date = new Date(System.currentTimeMillis());
        try{
            System.out.println("Record Controller "+date);
            returnObj.put("data", recordService.createDefaultRecordForAllEmployees(date));
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<Map<String,Object>> getRecord(HttpServletRequest request, @PathVariable("recordId") Integer recordId) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            Record record = recordService.getRecordById(recordId);
            returnObj.put("data", record);
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRecord/{employeeId}/{departmentId}")
    public ResponseEntity<Map<String,Object>> getRecord(HttpServletRequest request, @PathVariable("employeeId") Integer employeeId, @PathVariable("departmentId") Integer departmentId) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.getPresentDatesOfEmployeeByDepartment(employeeId, departmentId));
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String,Object>> getRecordsOfEmployee(HttpServletRequest request, @PathVariable("employeeId") Integer employeeId) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.getRecordsOfEmployee(employeeId));
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String,Object>> getAllRecords(HttpServletRequest request) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.getAllRecords());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/all/{date}")
    public ResponseEntity<Map<String,Object>> getAllRecords(HttpServletRequest request,@PathVariable Date date) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.getAllRecordsByDate(date));
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

    @GetMapping("/present/{employeeId}")
    public ResponseEntity<Map<String,Object>> getPresentDatesOfEmployee(HttpServletRequest request, @PathVariable("employeeId") Integer employeeId) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.getPresentDatesOfEmployee(employeeId));
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/onsite/{employeeId}")
    public ResponseEntity<Map<String,Object>> getOnSiteDatesOfEmployee(HttpServletRequest request, @PathVariable("employeeId") Integer employeeId) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.getOnSiteDatesOfEmployee(employeeId));
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
