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

    private static final String SQL_ADMIN_FIND_ALL = "select * from account where role = 'admin'";

    private static final String SQL_PENDING_ADMIN_FIND_ALL = "select * from account where adminrequestpending = true";

    private static final String SQL_MAKE_ADMIN = "UPDATE ACCOUNT SET ROLE = 'admin' , ADMINREQUESTPENDING = FALSE WHERE ACCOUNTID = ?";

    private static final String SQL_DECLINE_ADMIN = "UPDATE ACCOUNT SET ADMINREQUESTPENDING = FALSE  WHERE ACCOUNTID = ?";

    private RowMapper<Account> accountRowMapper = ((rs, rowNum) -> {
        return new Account(
                rs.getInt("ACCOUNTID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("ROLE"),
                rs.getBoolean("ADMINREQUESTPENDING"));
    });

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query(SQL_ADMIN_FIND_ALL, accountRowMapper);
    }

    @Override
    public List<Account> findAllPending() {
        return jdbcTemplate.query(SQL_PENDING_ADMIN_FIND_ALL, accountRowMapper);
    }

    @Override
    public void makeAdmin(Integer accountId) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_MAKE_ADMIN, accountId);
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @Override
    public void declineAdmin(Integer accountId) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_DECLINE_ADMIN, accountId);
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid Request");
        }
    }
}
