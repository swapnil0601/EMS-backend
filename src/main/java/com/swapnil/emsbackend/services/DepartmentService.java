package com.swapnil.emsbackend.services;

import java.util.List;

import com.swapnil.emsbackend.models.Department;
import com.swapnil.emsbackend.models.Employee;

public interface DepartmentService {
    Department add(String departmentName) throws RuntimeException;
    
    List<Department> getAll() throws RuntimeException;
    Department getById(Integer departmentId) throws RuntimeException;
    List<Employee> getAssignedEmployees(Integer departmentId) throws RuntimeException;

    Department update(Integer departmentId, Department department) throws RuntimeException;

    void delete(Integer departmentId) throws RuntimeException;

}
