package com.swapnil.emsbackend.services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.repositories.EmployeeRepository;
import com.swapnil.emsbackend.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() throws RuntimeException {
        try{
            return employeeRepository.findAll();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch all employees");
        }
    }

    @Override
    public Employee getEmployeeById(Integer employeeId) throws RuntimeException {
        try{
            return employeeRepository.findById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch employee by id");
        }
    }

    @Override
    public Employee addEmployee(Integer accountId) throws RuntimeException {
        try{
            int employeeId = employeeRepository.create(accountId);
            return employeeRepository.findById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to add employee");
        }
    }

    @Override
    public Employee updateEmployee(Integer employeeId, Employee employee) throws RuntimeException {
        try{
            employeeRepository.update(employeeId);
            return employeeRepository.findById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to update employee");
        }
    }

    @Override
    public void deleteEmployee(Integer employeeId) throws RuntimeException {
        try{
            employeeRepository.removeById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to delete employee");
        }
    }

    @Override
    public Integer getEmployeeIdFromAccountId(Integer accountId) throws RuntimeException {
        try{
            return employeeRepository.getEmployeeIdFromAccountId(accountId);
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch employee id from account id");
        }
    }
}
