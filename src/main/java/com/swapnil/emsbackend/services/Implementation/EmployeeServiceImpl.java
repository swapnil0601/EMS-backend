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
    public Employee add(Integer accountId) throws RuntimeException {
        try{
            int employeeId = employeeRepository.create(accountId);
            return employeeRepository.findById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to add employee");
        }
    }

    @Override
    public List<Employee> getAll() throws RuntimeException {
        try{
            return employeeRepository.findAll();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch all employees");
        }
    }

    @Override
    public Employee getById(Integer employeeId) throws RuntimeException {
        try{
            return employeeRepository.findById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch employee by id");
        }
    }

    @Override
    public Integer getIdFromAccountId(Integer accountId) throws RuntimeException {
        try{
            return employeeRepository.findIdFromAccountId(accountId);
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch employee id from account id");
        }
    }

   
    @Override
    public Employee update(Integer employeeId, Employee employee) throws RuntimeException {
        try{
            employeeRepository.update(employeeId);
            return employeeRepository.findById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to update employee");
        }
    }

    @Override
    public void delete(Integer employeeId) throws RuntimeException {
        try{
            employeeRepository.removeById(employeeId);
        }catch(Exception e){
            throw new RuntimeException("Failed to delete employee");
        }
    }
    
}
