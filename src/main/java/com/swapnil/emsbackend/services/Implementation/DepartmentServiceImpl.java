package com.swapnil.emsbackend.services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.models.Department;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.repositories.DepartmentRepository;
import com.swapnil.emsbackend.services.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    
    @Autowired
    DepartmentRepository departmentRepository;

   @Override
    public Department add(String departmentName) throws RuntimeException {
        try{
            Integer departmentId = departmentRepository.create(departmentName);
            return departmentRepository.findById(departmentId);
        }catch(Exception e){
            throw new RuntimeException("Failed to add department");
        }
    }


    @Override
    public List<Department> getAll() throws RuntimeException {
        try{
            return departmentRepository.findAll();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch all departments");
        }
    }

    @Override
    public Department getById(Integer departmentId) throws RuntimeException {
        try{
            return departmentRepository.findById(departmentId);
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch department by id");
        }
    }

    @Override
    public List<Employee> getAssignedEmployees(Integer departmentId) throws RuntimeException {
        try{
            return departmentRepository.findAssignedEmployees(departmentId);
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch assigned employees");
        }
    }


    @Override
    public Department update(Integer departmentId, Department department) throws RuntimeException {
        try{
            departmentRepository.update(departmentId, department);
            return departmentRepository.findById(departmentId);
        }catch(Exception e){
            throw new RuntimeException("Failed to update department");
        }
    }


    @Override
    public void delete(Integer departmentId) throws RuntimeException {
        try{
            departmentRepository.removeById(departmentId);
        }catch(Exception e){
            throw new RuntimeException("Failed to delete department");
        }
    }

    
}
