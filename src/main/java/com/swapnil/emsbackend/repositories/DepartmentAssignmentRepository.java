package com.swapnil.emsbackend.repositories;

import java.sql.Date;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.DepartmentAssignment;

public interface DepartmentAssignmentRepository {
    DepartmentAssignment findById(Integer assignmentId) throws NotFoundException;

    Integer create(Integer departmentId, Integer employeeId, Date assignmentDate) throws InvalidRequestException;

    Integer findIdByEmployeeIdDepartmentId(Integer employeeId,Integer departmentId) throws InvalidRequestException;

    void delete(Integer assignmentId) throws NotFoundException;

    void deleteByEmployeeIdDepartmentId(Integer employeeId, Integer departmentId) throws NotFoundException;
}
