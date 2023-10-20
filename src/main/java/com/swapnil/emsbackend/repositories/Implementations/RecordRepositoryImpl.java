package com.swapnil.emsbackend.repositories.Implementations;

import java.sql.Date;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Record;
import com.swapnil.emsbackend.repositories.RecordRepository;

@Repository
public class RecordRepositoryImpl implements RecordRepository {


    public final static String SQL_CREATE_RECORD = "INSERT INTO record (employeeid,departmentid,recorddate,present,onsite,donesyncupcall) VALUES (:employeeId,:departmentId,:date,:present,:onsite,:doneSyncUpCall)";

    public final static String SQL_CREATE_DEFAULT_RECORD = "INSERT INTO record (employeeid,departmentid,recorddate,present,onsite,donesyncupcall) VALUES (?,?,?,false,false,false)";

    public final static String SQL_CREATE_DEFAULT_RECORD_FOR_ALL_EMPLOYEES = "INSERT INTO record (employeeid,departmentid,recorddate,present,onsite,donesyncupcall) SELECT employee.employeeid,employee.departmentid,?,false,false,false FROM employee";

    public final static String SQL_FIND_RECORD_BY_ID = "SELECT * FROM RECORD WHERE RECORDID = ?";

    public final static String SQL_FIND_ALL_RECORDS = "SELECT * FROM record";

    public final static String SQL_FIND_ALL_RECORDS_BY_EMPLOYEE_ID = "SELECT * FROM record WHERE employeeid = ?";

    public final static String SQL_FIND_BY_EMPLOYEEID_DATE = "SELECT * FROM record WHERE employeeid = ? AND recorddate = ?";

    public final static String SQL_FIND_ALL_RECORDS_BY_DATE_WITH_EMPLOYEE_INFO = "SELECT record.*,employee.employeename FROM record INNER JOIN employee ON record.employeeid = employee.employeeid WHERE recorddate = ?";

    public final static String SQL_FETCH_PRESENT_FOR_EMPLOYEE_ID = "SELECT date FROM record WHERE employeeid = ? AND present = true";

    public final static String SQL_FETCH_ON_SITE_FOR_EMPLOYEE_ID = "SELECT date FROM record WHERE employeeid = ? AND onsite = true";

    public final static String SQL_UPDATE_RECORD_BY_ID = "UPDATE record SET present = ?,onsite = ?,donesyncupcall = ? WHERE recordid = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Override
    public Record create(Integer employeeId, Integer departmentId, Date date, boolean present, boolean onsite,
            boolean doneSyncUpCall) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(SQL_CREATE_RECORD,
                    new MapSqlParameterSource("employeeId", employeeId).addValue("departmentId", departmentId)
                            .addValue("date", date).addValue("present", present).addValue("onsite", onsite)
                            .addValue("doneSyncUpCall", doneSyncUpCall),
                    keyHolder);
            Integer recordId = (Integer) keyHolder.getKeys().get("recordid");
            return findById(recordId);
            
        } catch (Exception e) {
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
    public Record findRecordByEmployeeIdDate(Integer employeeId, Date date) throws InvalidRequestException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_EMPLOYEEID_DATE, recordRowMapper, new Object[]{employeeId,date});
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public Integer createDefault(Integer employeeId, Integer departmentId, Date date) throws InvalidRequestException {
        try {
            return jdbcTemplate.update(SQL_CREATE_DEFAULT_RECORD, new Object[] { employeeId, departmentId, date });
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
    public Integer createDefaultForAllEmployees(Date date) throws InvalidRequestException {
        try {
            return jdbcTemplate.update(SQL_CREATE_DEFAULT_RECORD_FOR_ALL_EMPLOYEES,new Object[]{date});
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
                rs.getDate("RECORDDATE"), rs.getBoolean("PRESENT"), rs.getBoolean("ONSITE"),
                rs.getBoolean("DONESYNCUPCALL"));
    });

    private RowMapper<Map<String,Object>> recordByDepartmentIdRowMapper = ((rs, rowNum) -> {
        return Map.of("recordId",rs.getInt("RECORDID"),"employeeId",rs.getInt("EMPLOYEEID"),"departmentId",rs.getInt("DEPARTMENTID"),
                "date",rs.getDate("RECORDDATE"),"present",rs.getBoolean("PRESENT"),"onSite",rs.getBoolean("ONSITE"),
                "doneSyncUpCall",rs.getBoolean("DONESYNCUPCALL"));
    });

    private RowMapper<Map<String,Object>> recordByDateWithEmployeeInfoRowMapper = ((rs, rowNum) -> {
        return Map.of("recordId",rs.getInt("RECORDID"),"employeeId",rs.getInt("EMPLOYEEID"),"departmentId",rs.getInt("DEPARTMENTID"),
                "date",rs.getDate("RECORDDATE"),"present",rs.getBoolean("PRESENT"),"onSite",rs.getBoolean("ONSITE"),
                "doneSyncUpCall",rs.getBoolean("DONESYNCUPCALL"),"employeeName",rs.getString("EMPLOYEENAME"));
    });

    private RowMapper<Date> dateRowMapper = ((rs, rowNum) -> {
        return rs.getDate("DATE");
    });


    @Override
public Record findById(Integer recordId) throws InvalidRequestException {
    try {
        return jdbcTemplate.queryForObject(SQL_FIND_RECORD_BY_ID, recordRowMapper,new Object[] { recordId });
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
    public List<Map<String,Object>> findAllByDate(Date date) throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL_RECORDS_BY_DATE_WITH_EMPLOYEE_INFO,recordByDateWithEmployeeInfoRowMapper,date);
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
    public Integer update(Integer recordId, boolean present,
            boolean onsite, boolean doneSyncUpCall) throws InvalidRequestException {
        try {
             jdbcTemplate.update(SQL_UPDATE_RECORD_BY_ID,new Object[]{present,onsite,doneSyncUpCall,recordId});
             return recordId;
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
