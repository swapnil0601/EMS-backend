package com.swapnil.emsbackend.repositories.Implementations;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Department;
import com.swapnil.emsbackend.repositories.DepartmentRepository;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {
    

    private static final String SQL_DEPARTMENT_CREATE = "INSERT INTO DEPARTMENT(DEPARTMENTNAME) VALUES(?);";

    private static final String SQL_FIND_ALL_DEPARTMENT = "SELECT * FROM DEPARTMENT;";

    private static final String SQL_FIND_DEPARTMENT_BY_ID = "SELECT * FROM DEPARTMENT WHERE DEPARTMENTID = ?;";

    private static final String SQL_FIND_ASSIGNED_EMPLOYEES = "SELECT DISTINCT EMPLOYEEID FROM EMPLOYEE WHERE DEPARTMENTID = ?;";

    private static final String SQL_UPDATE_DEPARTMENT = "UPDATE DEPARTMENT SET DEPARTMENTNAME = ? WHERE DEPARTMENTID = ?;";

    private static final String SQL_DELETE_DEPARTMENT = "DELETE FROM DEPARTMENT WHERE DEPARTMENTID = ?;";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Department> departmentRowMapper = ((rs, rowNum) -> {
        return new Department(rs.getInt("DEPARTMENTID"), rs.getString("DEPARTMENTNAME"));
    });

    public RowMapper<Integer> assignedEmployeesRowMapper = ((rs, rowNum) -> {
        return rs.getInt("EMPLOYEEID");
    });

    @Override
    public Integer create(String departmentName) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_DEPARTMENT_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, departmentName);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Department> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL_DEPARTMENT, departmentRowMapper);
    }

    @Override
    public Department findById(Integer departmentId) throws InvalidRequestException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_DEPARTMENT_BY_ID,departmentRowMapper, new Object[] { departmentId });
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Integer> findAssignedEmployees(Integer departmentId) throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_FIND_ASSIGNED_EMPLOYEES, assignedEmployeesRowMapper, new Object[] { departmentId });
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer departmentId, Department department) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE_DEPARTMENT, new Object[] { department.getDepartmentName(), departmentId });
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer departmentId) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_DELETE_DEPARTMENT, new Object[] { departmentId });
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }
}
