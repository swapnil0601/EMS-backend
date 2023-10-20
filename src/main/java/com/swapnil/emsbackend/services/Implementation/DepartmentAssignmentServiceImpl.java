package com.swapnil.emsbackend.services.Implementation;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
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
    public Integer getDepartmentIdFromEmployeeId(Integer employeeId) throws NotFoundException {
        try{
            System.out.println("Assign Service "+employeeId);
            return departmentAssignmentRepository.getDepartmentIdFromEmployeeId(employeeId);
        }catch(Exception e){
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public List<Map<String,Object>> getAssignmentInfo() throws InvalidRequestException {
        try{
            return departmentAssignmentRepository.findAssignmentInfo();
        }
        catch(Exception e){
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Map<String,Object>> getUnassignedEmployees() throws InvalidRequestException {
        try{
            return departmentAssignmentRepository.findUnassignedEmployees();
        }
        catch(Exception e){
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public DepartmentAssignment getDepartmentAssignmentByEmployeeId(Integer employeeId) throws NotFoundException {
        try{
            return departmentAssignmentRepository.findAssignmentByEmployeeId(employeeId);
        }
        catch(Exception e){
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public DepartmentAssignment updateDepartmentAssignment(DepartmentAssignment departmentAssignment)
            throws InvalidRequestException {
        try{
            Date currentDate = new Date(System.currentTimeMillis());
            int assignmentId=departmentAssignmentRepository.update(departmentAssignment.getAssignmentId(),departmentAssignment.getDepartmentId(),departmentAssignment.getEmployeeId(), currentDate);
            return departmentAssignmentRepository.findById(assignmentId);
        }
        catch(Exception e){
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public DepartmentAssignment addDepartmentAssignment(Integer employeeId, Integer departmentId) {
       try{
            Date currentDate = new Date(System.currentTimeMillis());
            int assignmentId=departmentAssignmentRepository.create(employeeId, departmentId, currentDate);
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
