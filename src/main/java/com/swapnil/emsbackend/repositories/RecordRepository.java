package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Record;

import java.sql.Date;

import java.util.Map;
import java.util.List;
public interface RecordRepository {
    Integer create(Integer employeeId,Integer departmentId,Date date,boolean present,boolean onsite,boolean doneSyncUpCall) throws InvalidRequestException;

    Integer createDefault(Integer employeeId,Integer departmentId,Date date) throws InvalidRequestException;

    Integer createDefaultForAllEmployees(Date date) throws InvalidRequestException;

    Record getRecordByEmployeeIdDate(Integer employeeId,Date date) throws InvalidRequestException;

    List<Map<String,Object>> findAll() throws InvalidRequestException;

    List<Map<String,Object>> findAllByDate(Date date) throws InvalidRequestException;

    Record findById(Integer recordId) throws InvalidRequestException;

    List<Map<String,Object>> findAllByEmployeeId(Integer employeeId) throws InvalidRequestException;

    List<Date> fetchPresentForEmployeeId(Integer employeeId) throws InvalidRequestException;

    List<Date> fetchOnSiteForEmployeeId(Integer employeeId) throws InvalidRequestException;

    Integer update(Integer recordId,Integer employeeId,Integer departmentId,Date date,boolean present,boolean onsite,boolean doneSyncUpCall) throws InvalidRequestException;
}