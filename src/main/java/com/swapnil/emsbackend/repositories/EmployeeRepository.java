package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Employee;

import java.util.List;


public interface EmployeeRepository {

    Integer create(Integer accountId) throws InvalidRequestException;

    List<Employee> findAll() throws InvalidRequestException;

    Employee findById(Integer employeeId) throws NotFoundException;

    Integer getEmployeeIdFromAccountId(Integer accountId) throws NotFoundException;

    void update(Integer employeeId) throws InvalidRequestException;

    void removeById(Integer employeeId) throws InvalidRequestException;
}
