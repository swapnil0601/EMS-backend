package com.swapnil.emsbackend.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Record {
    @Id
    @SequenceGenerator(
            name = "record_sequence",
            sequenceName = "record_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "record_sequence"
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate date;
    private boolean present;
    private boolean onSite;
    private boolean doneSyncUpCall;

    public Record() {
    }

    public Record(Employee employee, LocalDate date, boolean present, boolean onSite, boolean doneSyncUpCall) {
        this.employee = employee;
        this.date = date;
        this.present = present;
        this.onSite = onSite;
        this.doneSyncUpCall = doneSyncUpCall;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", employee=" + employee +
                ", date=" + date +
                ", present=" + present +
                ", onSite=" + onSite +
                ", doneSyncUpCall=" + doneSyncUpCall +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
