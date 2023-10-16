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
        System.out.println("Service Layer Add Record");
        try{
            int affectedRows=recordRepository.create(employeeId, departmentId, date, present, onsite, doneSyncUpCall);
            return recordRepository.getRecordByEmployeeIdDate(employeeId, date);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public Record addDefaultRecord(Integer employeeId, Integer departmentId, Date date) throws NotFoundException{
        try{
            Date currentDate = new Date(System.currentTimeMillis());
            int recordId=recordRepository.createDefault(employeeId, departmentId, currentDate);
            return recordRepository.findById(recordId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Map<String, Object>> createDefaultRecordForAllEmployees(Date date) throws NotFoundException{
        try{
            List<Map<String, Object>> records=recordRepository.findAllByDate(date);
            System.out.println("Service Layer: Created Records "+date);
            if(records.isEmpty())
            {
                recordRepository.createDefaultForAllEmployees(date);
                records=recordRepository.findAllByDate(date);
            }
            return records;
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
    public List<Date> getPresentDatesOfEmployee(Integer employeeId) throws NotFoundException{
        try{
            return recordRepository.fetchPresentForEmployeeId(employeeId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Date> getOnSiteDatesOfEmployee(Integer employeeId) throws NotFoundException{
        try{
            return recordRepository.fetchOnSiteForEmployeeId(employeeId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public List<Date> getPresentDatesOfEmployeeByDepartment(Integer employeeId, Integer departmentId)
            throws NotFoundException{
        try{
            return recordRepository.fetchPresentForEmployeeId(employeeId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }

    @Override
    public Record updateRecord(Integer recordId, Integer employeeId, Integer departmentId, String date, boolean present,
            boolean onsite, boolean doneSyncUpCall) throws NotFoundException{
        try{
            Date currentDate = new Date(System.currentTimeMillis());
            recordRepository.update(recordId, employeeId, departmentId, currentDate, present, onsite, doneSyncUpCall);
            return recordRepository.findById(recordId);
        }
        catch(Exception e){
            throw new NotFoundException("Record not found");
        }
    }
}
