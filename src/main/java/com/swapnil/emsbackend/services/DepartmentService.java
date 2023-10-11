package com.swapnil.emsbackend.services;

import java.util.List;

import com.swapnil.emsbackend.models.Department;
import com.swapnil.emsbackend.models.Employee;

public interface DepartmentService {
    List<Department> getAllDepartments() throws RuntimeException;

    Department getDepartmentById(Integer departmentId) throws RuntimeException;

    Department addDepartment(String departmentName) throws RuntimeException;

    Department updateDepartment(Integer departmentId, Department department) throws RuntimeException;

    void deleteDepartment(Integer departmentId) throws RuntimeException;

    List<Employee> getAssignedEmployees(Integer departmentId) throws RuntimeException;
}
