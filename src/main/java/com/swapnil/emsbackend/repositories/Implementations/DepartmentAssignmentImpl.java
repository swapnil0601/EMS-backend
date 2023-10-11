package com.swapnil.emsbackend.repositories.Implementations;

import com.swapnil.emsbackend.models.DepartmentAssignment;

import com.swapnil.emsbackend.repositories.DepartmentAssignmentRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;

@Repository
public class DepartmentAssignmentImpl implements DepartmentAssignmentRepository{
    
    private final String SQL_FIND_BY_ID = "SELECT * FROM department_assignment WHERE assignment_id = ?";

    private final String SQL_CREATE = "INSERT INTO department_assignment (department_id, employee_id, assignment_date) VALUES (?, ?, ?)";

    private final String SQL_DELETE = "DELETE FROM department_assignment WHERE assignment_id = ?";

    private final String SQL_DELETE_BY_EMPLOYEE_ID_DEPARTMENT_ID = "DELETE FROM department_assignment WHERE employee_id = ? AND department_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<DepartmentAssignment> departmentAssignmentRowMapper = ((rs, rowNum) -> {
        return new DepartmentAssignment(
            rs.getInt("assignment_id"),
            rs.getInt("department_id"),
            rs.getInt("employee_id"),
            rs.getDate("assignment_date")
        );
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
    public Integer create(Integer departmentId, Integer employeeId, Date date) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, departmentId);
                ps.setInt(2, employeeId);
                ps.setDate(3, date);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (DataIntegrityViolationException e) {
            if(e.getMessage().contains("department_assignment_employee_id_fkey"))
                throw new InvalidRequestException("Employee ID does not exist");
            else if(e.getMessage().contains("department_assignment_department_id_fkey"))
                throw new InvalidRequestException("Department ID does not exist");
            else
                throw new InvalidRequestException("Invalid Request");
        }
        catch (Exception e) {
            throw new InvalidRequestException("Invalid Request");
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
