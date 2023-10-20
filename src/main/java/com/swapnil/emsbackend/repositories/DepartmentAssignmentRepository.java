package com.swapnil.emsbackend.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.DepartmentAssignment;

public interface DepartmentAssignmentRepository {
    
    Integer create(Integer departmentId, Integer employeeId, Date assignmentDate) throws InvalidRequestException;
    
    DepartmentAssignment findById(Integer assignmentId) throws NotFoundException;
    
    //Deprecated
    Integer getDepartmentIdFromEmployeeId(Integer employeeId) throws NotFoundException;
    
    //Deprecated
    Integer findIdByEmployeeIdDepartmentId(Integer employeeId,Integer departmentId) throws InvalidRequestException;

    DepartmentAssignment findAssignmentByEmployeeId(Integer employeeId) throws InvalidRequestException;
    
    List<Map<String,Object>> findAssignmentInfo() throws InvalidRequestException;
    
    List<Map<String,Object>> findUnassignedEmployees() throws InvalidRequestException;

    Integer update(Integer assignmentId, Integer departmentId, Integer employeeId, Date assignmentDate) throws InvalidRequestException;

    void delete(Integer assignmentId) throws NotFoundException;

    void deleteByEmployeeIdDepartmentId(Integer employeeId, Integer departmentId) throws NotFoundException;
}
