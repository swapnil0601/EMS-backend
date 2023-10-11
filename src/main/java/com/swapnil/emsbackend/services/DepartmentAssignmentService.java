package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.DepartmentAssignment;

public interface DepartmentAssignmentService {
    DepartmentAssignment getById(Integer departmentAssignmentId) throws NotFoundException;

    DepartmentAssignment addDepartmentAssignment(Integer employeeId, Integer departmentId)
            throws InvalidRequestException;

    void deleteDepartmentAssignment(Integer departmentAssignmentId) throws NotFoundException;
}
