package com.swapnil.emsbackend.repositories;

import java.util.List;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Department;
import com.swapnil.emsbackend.models.Employee;

public interface DepartmentRepository {
    Integer create(String departmentName) throws InvalidRequestException;

    List<Department> findAll();
    Department findById(Integer departmentId) throws NotFoundException;
    List<Employee> findAssignedEmployees(Integer departmentId) throws InvalidRequestException;

    void update(Integer departmentId, Department department) throws InvalidRequestException;

    void removeById(Integer departmentId) throws InvalidRequestException;
    
    
}
