package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Employee;

import java.util.List;


public interface EmployeeRepository {

    Integer create(Integer userId) throws InvalidRequestException;

    List<Employee> findAll() throws InvalidRequestException;

    Employee findById(Integer employeeId) throws NotFoundException;

    Integer getEmployeeIdFromUserId(Integer userId) throws NotFoundException;

    void update(Integer employeeId) throws InvalidRequestException;

    void removeById(Integer employeeId) throws InvalidRequestException;
}
