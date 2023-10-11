package com.swapnil.emsbackend.repositories.Implementations;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.repositories.EmployeeRepository;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{
    
    private static final String SQL_EMPLOYEE_CREATE = "INSERT INTO EMPLOYEE(USERID ) VALUES(?);";

    private static final String SQL_FIND_ALL_EMPLOYEE = " SELECT EMPLOYEEID , E.USERID , FIRSTNAME , LASTNAME , EMAIL , ROLE FROM EMPLOYEE E JOIN USER U WHERE E.USERID=U.USERID;";

    private static final String SQL_FIND_EMPLOYEE_BY_ID = " SELECT EMPLOYEEID , E.USERID , FIRSTNAME , LASTNAME , EMAIL , ROLE FROM EMPLOYEE E JOIN USER U WHERE E.USERID=U.USERID AND EMPLOYEEID = ?;";

    private static final String SQL_FETCH_EMPLOYEEID_FOR_USERID = "SELECT EMPLOYEEID FROM EMPLOYEE WHERE USERID = ?;";

    private static final String SQL_DELETE_EMPLOYEE = "DELETE FROM EMPLOYEE WHERE EMPLOYEEID = ?";

    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE EMPLOYEE SET FIRSTNAME=?,LASTNAME=?,EMAIL=? WHERE EMPLOYEEID=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Employee> employeeRowMapper = ((rs, rowNum) -> {
        return new Employee(
                rs.getInt("USERID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("ROLE"),
                rs.getInt("EMPLOYEEID"));
    });
    private RowMapper<Integer> employeeIdRowMapper = ((rs,rowNum) -> {
        return rs.getInt("EMPLOYEEID");
    });

    @Override
    public Integer create(Integer userId)
            throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_EMPLOYEE_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public List<Employee> findAll() throws InvalidRequestException{
        try{
            return jdbcTemplate.query(SQL_FIND_ALL_EMPLOYEE, employeeRowMapper,new Object[]{});
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public Employee findById(Integer employeeId) throws NotFoundException,InvalidRequestException{
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_EMPLOYEE_BY_ID,employeeRowMapper,new Object[]{employeeId});
        }catch(EmptyResultDataAccessException e){
            throw new NotFoundException("Employee Not Found");
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public Integer getEmployeeIdFromUserId(Integer userId) throws NotFoundException,InvalidRequestException{
        try{
            return jdbcTemplate.queryForObject(SQL_FETCH_EMPLOYEEID_FOR_USERID,employeeIdRowMapper,new Object[]{userId});
        }catch(EmptyResultDataAccessException e){
            throw new NotFoundException("Employee Not Found");
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public void update(Integer employeeId) throws InvalidRequestException{
        try{
            jdbcTemplate.update(SQL_UPDATE_EMPLOYEE,new Object[]{employeeId});
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public void removeById(Integer employeeId) throws InvalidRequestException{
        try{
            jdbcTemplate.update(SQL_DELETE_EMPLOYEE,new Object[]{employeeId});
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }
}
