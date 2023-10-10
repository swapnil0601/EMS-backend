package com.swapnil.emsbackend.models;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table
public class DepartmentAssignment {

    @Id
    @SequenceGenerator(
            name = "department_assignment_sequence",
            sequenceName = "department_assignment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_assignment_sequence")
    int assignmentId;
    int departmentId, employeeId;
    Date assignmentDate;

    public DepartmentAssignment(int assignmentId, int departmentId, int employeeId, Date assignmentDate) {
        this.assignmentId = assignmentId;
        this.departmentId = departmentId;
        this.employeeId = employeeId;
        this.assignmentDate = assignmentDate;
    }

    public int getEnrollmentId() {
        return assignmentId;
    }
    public void setEnrollmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    public int getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId){
        this.employeeId = employeeId;
    }
    public Date getAssignmentDate() {
        return assignmentDate;
    }
    public void setAssignmentDate(Date assignmentDate){
        this.assignmentDate = assignmentDate;
    }

}
