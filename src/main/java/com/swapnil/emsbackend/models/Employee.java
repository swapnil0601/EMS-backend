package com.swapnil.emsbackend.models;

import jakarta.persistence.*;

@Entity
@Table
public class Employee extends User {
    @Id
    @SequenceGenerator(
            name = "employee_sequence",
            sequenceName = "employee_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employee_sequence"
    )
    private long employeeId;

    public Employee() {
    }

    public Employee(Integer userId, String firstName, String lastName, String email, String password, String role,int employeeId) {
        super(userId,firstName, lastName, email, password, role);
        this.employeeId = employeeId;
    }

    public Employee(Integer userId, String firstName, String lastName, String email, String role,int employeeId) {
        super(userId,firstName, lastName, email, role);
        this.employeeId = employeeId;
    }

    public Employee(int employeeId){
        this.employeeId = employeeId;
    }

    public int getStudentId() {
        return (int) employeeId;
    }

    public void setStudentId(int employeeId) {
        this.employeeId = employeeId;
    }
}
