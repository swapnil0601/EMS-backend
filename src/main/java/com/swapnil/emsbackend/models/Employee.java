package com.swapnil.emsbackend.models;
public class Employee extends User {
    private long employeeId;

    public Employee() {
    }

    public Employee(Integer userId, String firstName, String lastName, String email, String password, String role,int employeeId) {
        super(userId,firstName, lastName, email, password, role);
        this.employeeId = employeeId;
    }

    public Employee(Integer userId, String firstName, String lastName, String email, String role,Integer employeeId) {
        super(userId,firstName, lastName, email, role);
        this.employeeId = employeeId;
    }

    public Employee(int employeeId){
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return (int) employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
