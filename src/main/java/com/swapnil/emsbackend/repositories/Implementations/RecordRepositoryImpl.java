package com.swapnil.emsbackend.repositories.Implementations;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Record;
import com.swapnil.emsbackend.repositories.RecordRepository;

@Repository
public class RecordRepositoryImpl implements RecordRepository {


    public final static String SQL_CREATE_RECORD = "INSERT INTO record (employee_id,department_id,date,present,on_site,done_sync_up_call) VALUES (?,?,?,?,?,?)";

    public final static String SQL_CREATE_DEFAULT_RECORD = "INSERT INTO record (employee_id,department_id,date,present,on_site,done_sync_up_call) VALUES (?,?,?,false,false,false)";

    public final static String SQL_UPDATE_RECORD = "UPDATE record SET employee_id = ?,department_id = ?,date = ?,present = ?,on_site = ?,done_sync_up_call = ? WHERE record_id = ?";

    public final static String SQL_FIND_ALL_RECORDS = "SELECT * FROM record";

    public final static String SQL_FIND_ALL_RECORDS_BY_EMPLOYEE_ID = "SELECT * FROM record WHERE employee_id = ?";

    public final static String SQL_FETCH_PRESENT_FOR_EMPLOYEE_ID = "SELECT date FROM record WHERE employee_id = ? AND present = true";

    public final static String SQL_FETCH_ON_SITE_FOR_EMPLOYEE_ID = "SELECT date FROM record WHERE employee_id = ? AND on_site = true";

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public Integer create(Integer employeeId, Integer departmentId, Date date, boolean present, boolean onsite,
            boolean doneSyncUpCall) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_RECORD, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, employeeId);
                ps.setInt(2, departmentId);
                ps.setDate(3, date);
                ps.setBoolean(4, present);
                ps.setBoolean(5, onsite);
                ps.setBoolean(6, doneSyncUpCall);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicate"))
                throw new InvalidRequestException("Attendance already marked");
            else if (e.getMessage().contains("foreign key"))
                throw new InvalidRequestException(
                        "Either Employee and/or Department does not exist . Or Employee is not assigned to the department.");
            else
                throw new InvalidRequestException("Invalid request");
        }
    }


    @Override
    public Integer createDefault(Integer employeeId, Integer departmentId, Date date) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_RECORD, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, employeeId);
                ps.setInt(2, departmentId);
                ps.setDate(3, date);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicate"))
                throw new InvalidRequestException("Attendance already marked");
            else if (e.getMessage().contains("foreign key"))
                throw new InvalidRequestException(
                        "Either Employee and/or Department does not exist . Or Employee is not assigned to the department.");
            else
                throw new InvalidRequestException("Invalid request");
        }
    }

    //Use This
    private RowMapper<Record> recordRowMapper = ((rs, rowNum) -> {
        return new Record(rs.getInt("RECORDID"), rs.getInt("EMPLOYEEID"), rs.getInt("DEPARTMENTID"),
                rs.getDate("DATE"), rs.getBoolean("PRESENT"), rs.getBoolean("ONSITE"),
                rs.getBoolean("DONESYNCUPCALL"));
    });

    private RowMapper<Map<String,Object>> recordByDepartmentIdRowMapper = ((rs, rowNum) -> {
        return Map.of("recordId",rs.getInt("RECORDID"),"employeeId",rs.getInt("EMPLOYEEID"),"departmentId",rs.getInt("DEPARTMENTID"),
                "date",rs.getDate("DATE"),"present",rs.getBoolean("PRESENT"),"onSite",rs.getBoolean("ONSITE"),
                "doneSyncUpCall",rs.getBoolean("DONESYNCUPCALL"));
    });

    private RowMapper<Date> dateRowMapper = ((rs, rowNum) -> {
        return rs.getDate("DATE");
    });


    @Override
    public Record findById(Integer recordId) throws InvalidRequestException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_ALL_RECORDS, recordRowMapper, recordId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Map<String,Object>> findAll() throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL_RECORDS,recordByDepartmentIdRowMapper);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Map<String,Object>> findAllByEmployeeId(Integer employeeId) throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL_RECORDS_BY_EMPLOYEE_ID,recordByDepartmentIdRowMapper,employeeId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Date> fetchPresentForEmployeeId(Integer employeeId) throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_FETCH_PRESENT_FOR_EMPLOYEE_ID,dateRowMapper,employeeId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Date> fetchOnSiteForEmployeeId(Integer employeeId) throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_FETCH_ON_SITE_FOR_EMPLOYEE_ID,dateRowMapper,employeeId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public Integer update(Integer recordId, Integer employeeId, Integer departmentId, Date date, boolean present,
            boolean onsite, boolean doneSyncUpCall) throws InvalidRequestException {
        try {
            return jdbcTemplate.update(SQL_UPDATE_RECORD,employeeId,departmentId,date,present,onsite,doneSyncUpCall,recordId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicate"))
                throw new InvalidRequestException("Attendance already marked");
            else if (e.getMessage().contains("foreign key"))
                throw new InvalidRequestException(
                        "Either Employee and/or Department does not exist . Or Employee is not assigned to the department.");
            else
                throw new InvalidRequestException("Invalid request");
        }
    }
}
