package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Record;

import java.sql.Date;

import java.util.Map;
import java.util.List;
public interface RecordRepository {
    Record create(Integer employeeId,Integer departmentId,Date date,boolean present,boolean onsite,boolean doneSyncUpCall) throws InvalidRequestException;
    
    Record findById(Integer recordId) throws InvalidRequestException;

    Record findRecordByEmployeeIdDate(Integer employeeId,Date date) throws InvalidRequestException;

    List<Map<String,Object>> findAll() throws InvalidRequestException;

    List<Map<String,Object>> findAllByDate(Date date) throws InvalidRequestException;

    List<Map<String,Object>> findAllByEmployeeId(Integer employeeId) throws InvalidRequestException;

    List<Map<String,Object>> fetchLast30DaysPresentCount() throws InvalidRequestException;

    List<Map<String,Object>> fetchLast30DaysReports() throws InvalidRequestException;

    Integer update(Integer recordId,boolean present,boolean onsite,boolean doneSyncUpCall) throws InvalidRequestException;
}