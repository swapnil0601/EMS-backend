package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.models.Record;
import com.swapnil.emsbackend.repositories.RecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Record getRecord(Long recordId){
        return recordRepository.findById(recordId).orElseThrow(()-> new IllegalStateException("Record Not Found"));
    }
    public List<Record> getAllRecords(){
        return recordRepository.findAll();
    }
    public List<Record> getAllRecordByEmployeeId(Long employeeId){
        return recordRepository.findAllByEmployeeId(employeeId);
    }
    public List<Record> findAllByDate(LocalDate date){
        return recordRepository.findAllByDate(date);
    }
//    public List<Map<String,Object>> getMonthlyAttendanceOfEmployee(Long employeeId){
//        return recordRepository.getMonthlyAttendanceCounts(employeeId);
//    }
    public void createDefaultRecordsForAllEmployees(LocalDate date) {
        recordRepository.createDefaultRecordForAllEmployeesOnDate(date);
    }



    @Transactional
    public Record updateRecord(Long recordId, boolean present, boolean onsite, boolean doneSyncUp){
        Record record = recordRepository.findById(recordId)
                .orElseThrow(()-> new IllegalStateException("Record ID not found"));

        record.setPresent(present);
        record.setOnSite(onsite);
        record.setDoneSyncUpCall(doneSyncUp);

        return record;
    }
}
