package com.swapnil.emsbackend.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Record;

public interface RecordService {
    Record addRecord(Integer employeeId, Integer departmentId, Date date, boolean present, boolean onsite,
            boolean doneSyncUpCall) throws NotFoundException;

    Record getRecordByEmployeeIdDate(Integer employeeId, Date date) throws NotFoundException;

    Record getRecordById(Integer recordId) throws NotFoundException;

    List<Map<String, Object>> fetchLast30DaysPresentCount() throws NotFoundException;

    List<Map<String, Object>> fetchLast30DaysReports() throws NotFoundException;

    List<Map<String, Object>> getRecordsOfEmployee(Integer employeeId) throws NotFoundException;

    List<Map<String, Object>> getAllRecords() throws NotFoundException;

    List<Map<String, Object>> getAllRecordsByDate(Date date) throws NotFoundException;

    Record updateRecord(Record record) throws NotFoundException;

}
