package com.swapnil.emsbackend.models;
public class Employee extends Account {
    private long employeeId;

    public Employee() {
    }

    public Employee(Integer accountId, String firstName, String lastName, String email, String password, String role,int employeeId) {
        super(accountId,firstName, lastName, email, password, role);
        this.employeeId = employeeId;
    }

    public Employee(Integer accountId, String firstName, String lastName, String email, String role,Integer employeeId) {
        super(accountId,firstName, lastName, email, role);
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
