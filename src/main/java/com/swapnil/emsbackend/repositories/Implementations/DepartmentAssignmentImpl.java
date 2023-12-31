package com.swapnil.emsbackend.repositories.Implementations;

import com.swapnil.emsbackend.models.DepartmentAssignment;

import com.swapnil.emsbackend.repositories.DepartmentAssignmentRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;

@Repository
public class DepartmentAssignmentImpl implements DepartmentAssignmentRepository{
    
    private final String SQL_FIND_BY_ID = "SELECT * FROM employeedepartment WHERE assignmentid = ?";

    private final String SQL_CREATE = "INSERT INTO employeedepartment (departmentid, employeeid, assignmentdate) VALUES (?, ?, ?)";

    private final String SQL_DELETE = "DELETE FROM employeedepartment WHERE assignmentid = ?";

    private final String SQL_DELETE_BY_EMPLOYEE_ID_DEPARTMENT_ID = "DELETE FROM employeedepartment WHERE employeeid = ? AND departmentid = ?";

    private final String SQL_ID_FROM_DEPARTMENTID_EMPLOYEEID = "SELECT ASSIGNMENTID FROM employeedepartment WHERE DEPARTMENTID = ? AND EMPLOYEEID = ?";

    private final String SQL_FIND_FROM_EMPLOYEEID = "SELECT * FROM employeedepartment WHERE EMPLOYEEID = ?";

    private final String SQL_UPDATE = "UPDATE employeedepartment SET departmentid = ?, assignmentdate = ? WHERE employeeid = ?";

    private final String SQL_DEPARTMENTID_FROM_EMPLOYEEID = "SELECT DEPARTMENTID FROM employeedepartment WHERE EMPLOYEEID = ?";

    private final String SQL_ASSIGNMENT_INFO = "Select e.employeeid,ed.assignmentid ,d.departmentid,CONCAT(a.firstName, ' ', a.lastName) AS fullname, ed.assignmentdate, d.departmentname from account a left join employee e on a.accountid=e.accountid left join employeedepartment ed on e.employeeid = ed.employeeid left join department d on ed.departmentid = d.departmentid where ed.assignmentid is not null";

    private final String SQL_UNASSIGNED_EMPLOYEES = "Select e.employeeid,CONCAT(a.firstName, ' ', a.lastName) AS fullname from account a left join employee e on a.accountid=e.accountid left join employeedepartment ed on e.employeeid = ed.employeeid where ed.assignmentid is null";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<DepartmentAssignment> departmentAssignmentRowMapper = ((rs, rowNum) -> {
        return new DepartmentAssignment(
            rs.getInt("assignmentid"),
            rs.getInt("departmentid"),
            rs.getInt("employeeid"),
            rs.getDate("assignmentdate")
        );
    });

    private RowMapper<Map<String,Object>> departmentAssignmentInfoRowMapper = ((rs, rowNum) -> {
        return Map.of(
            "employeeId",rs.getInt("employeeid"),
            "assignmentId",rs.getInt("assignmentid"),
            "departmentId",rs.getInt("departmentid"),
            "fullName",rs.getString("fullname"),
            "assignmentDate",rs.getDate("assignmentdate"),
            "departmentName",rs.getString("departmentname")
        );
    });

    private RowMapper<Map<String,Object>> unassignedEmployeesRowMapper = ((rs, rowNum) -> {
        return Map.of(
            "employeeId",rs.getInt("employeeid"),
            "fullName",rs.getString("fullname")
        );
    });

    private RowMapper<Integer> departmentAssignmentIdRowMapper = ((rs, rowNum) -> {
        return rs.getInt("assignmentid");
    });
    private RowMapper<Integer> departmentIdRowMapper = ((rs, rowNum) -> {
        return rs.getInt("departmentid");
    });

    @Override
    public DepartmentAssignment findById(Integer assignmentId) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,departmentAssignmentRowMapper, new Object[]{assignmentId});
        } catch (Exception e) {
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public List<Map<String,Object>> findAssignmentInfo() throws InvalidRequestException {
        try{
            return jdbcTemplate.query(SQL_ASSIGNMENT_INFO,departmentAssignmentInfoRowMapper);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Map<String,Object>> findUnassignedEmployees() throws InvalidRequestException {
        try{
            return jdbcTemplate.query(SQL_UNASSIGNED_EMPLOYEES,unassignedEmployeesRowMapper);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public DepartmentAssignment findAssignmentByEmployeeId(Integer employeeId) throws InvalidRequestException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_FROM_EMPLOYEEID,departmentAssignmentRowMapper, new Object[]{employeeId});
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override 
    public Integer findIdByEmployeeIdDepartmentId(Integer employeeId,Integer departmentId) throws InvalidRequestException {
        try{
            return jdbcTemplate.queryForObject(SQL_ID_FROM_DEPARTMENTID_EMPLOYEEID, departmentAssignmentIdRowMapper, new Object[] { departmentId, employeeId });
        }
        catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Integer getDepartmentIdFromEmployeeId(Integer employeeId) throws NotFoundException {
        try {
            System.out.println("Assign Repo "+employeeId);
            return jdbcTemplate.queryForObject(SQL_DEPARTMENTID_FROM_EMPLOYEEID, departmentIdRowMapper, employeeId);
        } catch (Exception e) {
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public Integer create(Integer employeeId, Integer departmentId, Date date) throws InvalidRequestException {
        try{
            jdbcTemplate.update(SQL_CREATE, new Object[] { departmentId, employeeId, date });
            return jdbcTemplate.queryForObject(SQL_ID_FROM_DEPARTMENTID_EMPLOYEEID, departmentAssignmentIdRowMapper, new Object[] { departmentId, employeeId });
        }
        catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public Integer update(Integer assignmentId, Integer departmentId, Integer employeeId, Date date) throws InvalidRequestException {
        try{
            jdbcTemplate.update(SQL_UPDATE, new Object[] { departmentId, date, employeeId });
            return assignmentId;
        }
        catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public void delete(Integer assignmentId) throws NotFoundException {
        try {
            jdbcTemplate.update(SQL_DELETE, assignmentId);
        } catch (Exception e) {
            throw new NotFoundException("Department Assignment not found");
        }
    }

    @Override
    public void deleteByEmployeeIdDepartmentId(Integer employeeId, Integer departmentId) throws NotFoundException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_EMPLOYEE_ID_DEPARTMENT_ID, employeeId, departmentId);
        } catch (Exception e) {
            throw new NotFoundException("Department Assignment not found");
        }
    }
}
