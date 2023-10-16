package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.DepartmentAssignment;

public interface DepartmentAssignmentService {
    DepartmentAssignment getById(Integer departmentAssignmentId) throws NotFoundException;

    DepartmentAssignment addDepartmentAssignment(Integer employeeId, Integer departmentId)
            throws InvalidRequestException;

    Integer getDepartmentIdFromEmployeeId(Integer employeeId) throws NotFoundException;

    void deleteDepartmentAssignment(Integer employeeId,Integer departmentId) throws NotFoundException;

    void deleteDepartmentAssignmentById(Integer departmentAssignmentId) throws NotFoundException;
}
