package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.models.Record;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface RecordRepository extends JpaRepository<Record,Long> {

    @Query("SELECT r FROM Record r WHERE r.employee.id = :employeeId")
    List<Record> findAllByEmployeeId(@Param("employeeId") Long employeeId);

    @Transactional
    @Modifying
    @Query("INSERT INTO Record (employee, date, present, onSite, doneSyncUpCall) " +
            "SELECT e, :date, false, false, false FROM Employee e " +
            "WHERE NOT EXISTS (SELECT 1 FROM Record r WHERE r.employee = e AND r.date = :date)")
    void createDefaultRecordForAllEmployeesOnDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Record r WHERE r.date = :date")
    List<Record> findAllByDate(@Param("date") LocalDate date);

}
