package com.swapnil.emsbackend.controllers;

import com.swapnil.emsbackend.models.Record;
import com.swapnil.emsbackend.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/record")
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public List<Record> getRecords(){return recordService.getAllRecords();}

//    @GetMapping(path = "monthly",params = "employeeId")
//    public List<Map<String,Object>> getMonthlyAttendanceOfEmployee(@RequestParam("employeeId") Long employeeId){
//        return recordService.getMonthlyAttendanceOfEmployee(employeeId);
//    }

    @GetMapping(params = "employeeId")
    public List<Record> getRecordsOfEmployee(@RequestParam("employeeId") Long employeeId){
        return recordService.getAllRecordByEmployeeId(employeeId);
    }

    @GetMapping(params = "recordId")
    public Record getRecordById(@RequestParam("recordId") Long recordId){
        return recordService.getRecord(recordId);
    }
    @GetMapping(params = "date")
    public List<Record> getRecordsOfEmployee(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return recordService.findAllByDate(date);
    }
    @PostMapping(params = "date")
    public List<Record> createRecordForAllOnDate(@RequestParam("date") LocalDate date){
        recordService.createDefaultRecordsForAllEmployees(date);

        return recordService.findAllByDate(date);
    }

    @PutMapping(path = "{recordId}")
    public Record updateRecord(
            @PathVariable("recordId") Long recordId,
            @RequestParam(required = false) Boolean present,
            @RequestParam(required = false) Boolean onsite,
            @RequestParam(required = false) Boolean doneSyncUp
    ){
        return recordService.updateRecord(recordId,present,onsite,doneSyncUp);
    }
}
