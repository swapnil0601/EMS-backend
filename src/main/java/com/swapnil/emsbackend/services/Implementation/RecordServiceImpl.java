package com.swapnil.emsbackend.services.Implementation;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.repositories.RecordRepository;
import com.swapnil.emsbackend.services.RecordService;
import com.swapnil.emsbackend.models.Record;

@Service
public class RecordServiceImpl implements RecordService{
    @Autowired
    RecordRepository recordRepository;

    @Override
    public Record addRecord(Integer employeeId, Integer departmentId, Date date, boolean present, boolean onsite,
            boolean doneSyncUpCall) throws NotFoundException {
        try{
            return recordRepository.create(employeeId, departmentId, date, present, onsite, doneSyncUpCall);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public Record getRecordById(Integer recordId) throws NotFoundException{
        try{
            return recordRepository.findById(recordId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Map<String, Object>> fetchLast30DaysPresentCount() throws NotFoundException{
        try{
            return recordRepository.fetchLast30DaysPresentCount();
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Map<String, Object>> fetchLast30DaysReports() throws NotFoundException{
        try{
            return recordRepository.fetchLast30DaysReports();
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public Record getRecordByEmployeeIdDate(Integer employeeId, Date date) throws NotFoundException{
        try{
            return recordRepository.findRecordByEmployeeIdDate(employeeId, date);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Map<String,Object>> getAllRecordsByDate(Date date) throws NotFoundException{
        try{
            return recordRepository.findAllByDate(date);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Map<String, Object>> getRecordsOfEmployee(Integer employeeId) throws NotFoundException{
        try{
            return recordRepository.findAllByEmployeeId(employeeId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Map<String, Object>> getAllRecords() throws NotFoundException{
        try{
            return recordRepository.findAll();
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public Record updateRecord(Record record) throws NotFoundException{
        try{
            int recordId = recordRepository.update(record.getRecordId(), record.getPresent(), record.getOnSite(), record.getDoneSyncUpCall());
            return recordRepository.findById(recordId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }
}
