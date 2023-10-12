package com.swapnil.emsbackend.repositories.Implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.repositories.AdminRepository;

@Repository
public class AdminRepositoryImpl implements AdminRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ASSIGN_ADMIN = "UPDATE ACCOUNT SET is_admin = true WHERE employee_id = ?";

    private static final String SQL_REMOVE_ADMIN = "UPDATE ACCOUNT SET is_admin = false WHERE employee_id = ?";

    private static final String SQL_GET_ALL_ADMINS = "SELECT * FROM ACCOUNT WHERE is_admin = true";

    private RowMapper<Account> accountRowMapper = ((rs, rowNum) -> {
        return new Account(
            rs.getInt("account_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("is_admin")
        );
    }); 

    @Override
    public void assignAdmin(Integer accountId) throws Exception {
        try {
            jdbcTemplate.update(SQL_ASSIGN_ADMIN, accountId);
        } catch (Exception e) {
            throw new Exception("Error assigning admin");
        }
    }

    @Override
    public void removeAdmin(Integer accountId) throws Exception {
        try {
            jdbcTemplate.update(SQL_REMOVE_ADMIN, accountId);
        } catch (Exception e) {
            throw new Exception("Error removing admin");
        }
    }

    @Override
    public List<Account> getAllAdmins() throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL_ADMINS, accountRowMapper, new Object[]{});
        } catch (Exception e) {
            throw new InvalidRequestException("Error getting all admins");
        }
    }
}
