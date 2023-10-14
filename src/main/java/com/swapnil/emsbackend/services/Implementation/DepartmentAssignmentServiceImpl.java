package com.swapnil.emsbackend.services.Implementation;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.DepartmentAssignment;
import com.swapnil.emsbackend.repositories.DepartmentAssignmentRepository;
import com.swapnil.emsbackend.services.DepartmentAssignmentService;

@Service
public class DepartmentAssignmentServiceImpl implements DepartmentAssignmentService{
    
    @Autowired
    DepartmentAssignmentRepository departmentAssignmentRepository;

    @Override
    public DepartmentAssignment getById(Integer departmentAssignmentId) throws NotFoundException {
        try{
            return departmentAssignmentRepository.findById(departmentAssignmentId);
        }catch(Exception e){
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public DepartmentAssignment addDepartmentAssignment(Integer employeeId, Integer departmentId) {
       try{
            Date currentDate = new Date(System.currentTimeMillis());
            int assignmentId=departmentAssignmentRepository.findIdByEmployeeIdDepartmentId(employeeId, departmentId);
            System.out.println("assignmentId: "+assignmentId);
            if(assignmentId==-1){
                assignmentId=departmentAssignmentRepository.create(employeeId, departmentId, currentDate);
            }
            return departmentAssignmentRepository.findById(assignmentId);
       }
         catch(Exception e){
              throw new NotFoundException("Department Assignment not found");
         }
    }

    @Override
    public void deleteDepartmentAssignment(Integer employeeId, Integer departmentId) throws NotFoundException {
        try{
            departmentAssignmentRepository.deleteByEmployeeIdDepartmentId(employeeId, departmentId);
        }catch(Exception e){
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public void deleteDepartmentAssignmentById(Integer departmentAssignmentId) throws NotFoundException {
        try{
            departmentAssignmentRepository.delete(departmentAssignmentId);
        }catch(Exception e){
            throw new NotFoundException("Department Assignment not found");
        }
    }
}
