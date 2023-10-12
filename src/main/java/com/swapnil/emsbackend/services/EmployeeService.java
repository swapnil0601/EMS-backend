package com.swapnil.emsbackend.services;

import java.util.List;

import com.swapnil.emsbackend.models.Employee;

public interface EmployeeService {
    List<Employee> getAllEmployees() throws RuntimeException;

    Employee getEmployeeById(Integer employeeId) throws RuntimeException;

    Employee addEmployee(Integer employeeId) throws RuntimeException;

    Employee updateEmployee(Integer employeeId, Employee employee) throws RuntimeException;

    void deleteEmployee(Integer employeeId) throws RuntimeException;

    Integer getEmployeeIdFromAccountId(Integer accountId) throws RuntimeException;
}
