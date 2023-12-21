package com.swapnil.emsbackend.models;
public class Account {
    
    Integer accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String  password;
    private String  role;
    private Boolean adminRequestPending;

    public Account() {

    }

    public Account(Integer accountId, String firstName, String lastName, String email, String password, String role,Boolean adminRequestPending) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.adminRequestPending=adminRequestPending;
    }

    public Account(Integer accountId, String firstName, String lastName, String email, String role,Boolean adminRequestPending) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.adminRequestPending=adminRequestPending;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdminRequestPending() {
        return adminRequestPending;
    }

    public void setAdminRequestPending(Boolean adminRequestPending) {
        this.adminRequestPending = adminRequestPending;
    }

}
