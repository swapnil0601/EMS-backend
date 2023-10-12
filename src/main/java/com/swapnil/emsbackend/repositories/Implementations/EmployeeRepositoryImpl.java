package com.swapnil.emsbackend.repositories.Implementations;

// import java.sql.PreparedStatement;
// import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
// import org.springframework.jdbc.support.GeneratedKeyHolder;
// import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.repositories.EmployeeRepository;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{
    
    private static final String SQL_EMPLOYEE_CREATE = "INSERT INTO EMPLOYEE(ACCOUNTID ) VALUES(?);";

    private static final String SQL_FIND_ALL_EMPLOYEE = " SELECT EMPLOYEEID , E.ACCOUNTID , FIRSTNAME , LASTNAME , EMAIL , ROLE FROM EMPLOYEE E JOIN ACCOUNT A ON E.ACCOUNTID=A.ACCOUNTID;";

    private static final String SQL_FIND_EMPLOYEE_BY_ID = " SELECT EMPLOYEEID , E.ACCOUNTID , FIRSTNAME , LASTNAME , EMAIL , ROLE FROM EMPLOYEE E JOIN ACCOUNT A ON E.ACCOUNTID=A.ACCOUNTID AND EMPLOYEEID = ?;";

    private static final String SQL_FETCH_EMPLOYEEID_FOR_ACCOUNTID = "SELECT EMPLOYEEID FROM EMPLOYEE WHERE ACCOUNTID = ?;";

    private static final String SQL_DELETE_EMPLOYEE = "DELETE FROM EMPLOYEE WHERE EMPLOYEEID = ?";

    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE EMPLOYEE SET FIRSTNAME=?,LASTNAME=?,EMAIL=? WHERE EMPLOYEEID=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Employee> employeeRowMapper = ((rs, rowNum) -> {
        return new Employee(
                rs.getInt("ACCOUNTID"),
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
    public Integer create(Integer accountId)
            throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_EMPLOYEE_CREATE, new Object[]{accountId});
            System.out.println("Repository Create" + accountId);
            return accountId;
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public List<Employee> findAll() throws InvalidRequestException{
        try{
            List<Employee> list = jdbcTemplate.query(SQL_FIND_ALL_EMPLOYEE, employeeRowMapper,new Object[]{});
            return list;
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public Employee findById(Integer employeeId) throws NotFoundException,InvalidRequestException{
        try{
            System.out.println("FIND BY ID "+employeeId);
            Employee employee = jdbcTemplate.queryForObject(SQL_FIND_EMPLOYEE_BY_ID,employeeRowMapper,new Object[]{employeeId});
            System.out.println("FIND BY ID "+employeeId);
            return employee;
        }catch(EmptyResultDataAccessException e){
            throw new NotFoundException("Employee Not Found");
        }catch(Exception e){
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public Integer getEmployeeIdFromAccountId(Integer accountId) throws NotFoundException,InvalidRequestException{
        try{
            return jdbcTemplate.queryForObject(SQL_FETCH_EMPLOYEEID_FOR_ACCOUNTID,employeeIdRowMapper,new Object[]{accountId});
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
