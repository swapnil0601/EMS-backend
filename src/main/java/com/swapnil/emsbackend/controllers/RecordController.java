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

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObj.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObj.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObj, HttpStatus.BAD_REQUEST);
        }

        try{
            String dateString = map.get("date").toString();
            LocalDate localDate = LocalDate.parse(dateString);
            Date date = Date.valueOf(localDate);
            Integer employeeId = (Integer) map.get("employeeId");
            Integer departmentId = departmentAssignmentService.getDepartmentIdFromEmployeeId(employeeId);
            Boolean present = (Boolean) map.get("present");
            Boolean onsite = (Boolean) map.get("onSite");
            Boolean doneSyncUpCall = (Boolean) map.get("doneSyncUpCall");

            Record existingRecord = recordService.getRecordByEmployeeIdDate(employeeId, date);

            Record newRecord;
            if(existingRecord!=null){
                existingRecord.setPresent(present);
                existingRecord.setOnSite(onsite);
                existingRecord.setDoneSyncUpCall(doneSyncUpCall);
                newRecord = recordService.updateRecord(existingRecord);
            }
            else{
                newRecord = recordService.addRecord(employeeId, departmentId, date, present, onsite, doneSyncUpCall);
            }
            returnObj.put("data", newRecord);
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

    @GetMapping("/last30days")
    public ResponseEntity<Map<String,Object>> getLast30DaysPresentCount(HttpServletRequest request) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.fetchLast30DaysPresentCount());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.OK);
        }
        catch(Exception e){
            returnObj.put("error",e.getMessage());
            return new ResponseEntity<Map<String,Object>>(returnObj,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/last30daysreport")
    public ResponseEntity<Map<String,Object>> getLast30DaysReports(HttpServletRequest request) {
        Map<String,Object> returnObj = new HashMap<>();

        try{
            returnObj.put("data", recordService.fetchLast30DaysReports());
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
}
