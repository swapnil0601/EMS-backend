package com.swapnil.emsbackend.services;

import java.util.List;

import com.swapnil.emsbackend.models.Employee;

public interface EmployeeService {
    Employee add(Integer employeeId) throws RuntimeException;

    List<Employee> getAll() throws RuntimeException;
    Employee getById(Integer employeeId) throws RuntimeException;
    Integer getIdFromAccountId(Integer accountId) throws RuntimeException;

    Employee update(Integer employeeId, Employee employee) throws RuntimeException;

    void delete(Integer employeeId) throws RuntimeException;

}
