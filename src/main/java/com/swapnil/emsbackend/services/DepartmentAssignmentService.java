package com.swapnil.emsbackend.services;

import java.util.List;
import java.util.Map;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.DepartmentAssignment;

public interface DepartmentAssignmentService {
    DepartmentAssignment getById(Integer departmentAssignmentId) throws NotFoundException;

    DepartmentAssignment addDepartmentAssignment(Integer employeeId, Integer departmentId)
            throws InvalidRequestException;

    DepartmentAssignment getDepartmentAssignmentByEmployeeId(Integer employeeId) throws NotFoundException;

    DepartmentAssignment updateDepartmentAssignment(DepartmentAssignment departmentAssignment)
            throws InvalidRequestException;

    Integer getDepartmentIdFromEmployeeId(Integer employeeId) throws NotFoundException;

    List<Map<String,Object>> getAssignmentInfo() throws InvalidRequestException;

    List<Map<String,Object>> getUnassignedEmployees() throws InvalidRequestException;

    void deleteDepartmentAssignment(Integer employeeId,Integer departmentId) throws NotFoundException;

    void deleteDepartmentAssignmentById(Integer departmentAssignmentId) throws NotFoundException;
}
