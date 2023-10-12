package com.swapnil.emsbackend.models;


import java.sql.Date;

public class Record {
    
    private int recordId,employeeId,departmentId;

    private Date date;
    private boolean present;
    private boolean onSite;
    private boolean doneSyncUpCall;

    public Record() {
    }

    public Record(int recordId,int employeeId,int departmentId, Date date, boolean present, boolean onSite, boolean doneSyncUpCall) {
        this.recordId = recordId;
        this.employeeId = employeeId;
        this.departmentId = departmentId;
        this.date = date;
        this.present = present;
        this.onSite = onSite;
        this.doneSyncUpCall = doneSyncUpCall;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int enrollmentId) {
        this.recordId = enrollmentId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int studentId) {
        this.employeeId = studentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int courseId) {
        this.departmentId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isOnSite() {
        return onSite;
    }

    public void setOnSite(boolean onSite) {
        this.onSite = onSite;
    }

    public boolean isDoneSyncUpCall() {
        return doneSyncUpCall;
    }

    public void setDoneSyncUpCall(boolean doneSyncUpCall) {
        this.doneSyncUpCall = doneSyncUpCall;
    }
}
