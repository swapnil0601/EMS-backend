package com.swapnil.emsbackend.repositories.Implementations;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.repositories.AccountRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    
    @Autowired 
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ACCOUNT_CREATE = "INSERT INTO ACCOUNT (FIRSTNAME,LASTNAME , EMAIL , PASSWORD , ROLE) VALUES(:firstName,:lastName,:email,:password,:role)";

    private static final String SQL_ACCOUNT_FIND_BY_ID = "SELECT * FROM ACCOUNT WHERE accountid = ?";

    private static final String
    SQL_ACCOUNT_FIND_BY_EMAIL 
    = "SELECT ACCOUNTID , FIRSTNAME , LASTNAME , EMAIL , PASSWORD , ROLE FROM ACCOUNT WHERE EMAIL = ?";

    private static final String SQL_ACCOUNT_UPDATE =
    "UPDATE ACCOUNT SET FIRSTNAME=? , LASTNAME=? , EMAIL=? , PASSWORD =? WHERE ACCOUNTID=?;";

    private RowMapper<Account> accountRowMapper = ((rs, rowNum) -> {
        return new Account(
                rs.getInt("ACCOUNTID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("ROLE"));
    });

    private RowMapper<Account> accountRowMapperWithPassword = ((rs, rowNum) -> {
        return new Account(
                rs.getInt("ACCOUNTID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("ROLE"));
    });

    @Override
    public Account create(String firstName, String lastName, String email, String password, String role) throws AuthException {

        Account createdAccount=null;
        System.out.println("Account Repository Layer "+firstName+" "+lastName+" "+email+" "+password+" "+role);
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("firstName", firstName);
            paramMap.put("lastName", lastName);
            paramMap.put("email", email);
            paramMap.put("password", hashPassword);
            paramMap.put("role", role);
            namedParameterJdbcTemplate.update(SQL_ACCOUNT_CREATE, new MapSqlParameterSource(paramMap), keyHolder);
            System.out.println("Account Created");
            Integer accountId = (Integer) keyHolder.getKeys().get("ACCOUNTID");
            createdAccount = findById(accountId);
        }catch
        (Exception e){
            throw new AuthException("Invalid details. Failed to create account");
        }
        return createdAccount;
    }

    @Override
    public Account findById(Integer accountId){
        return jdbcTemplate.queryForObject(SQL_ACCOUNT_FIND_BY_ID, accountRowMapper, new Object[] { accountId });
    }
    
    @Override
    public Account findByEmail(String email) throws AuthException {
        try {
            Account account = jdbcTemplate.queryForObject(SQL_ACCOUNT_FIND_BY_EMAIL, accountRowMapper,
                    new Object[] { email });
            return account;
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException("Invalid Credentials");
        }
    }

    @Override
    public Account findByEmailAndPassword(String email, String password) throws AuthException {
        try {
            System.out.println("Account Repository Layer "+email+" "+password);
            Account account = jdbcTemplate.queryForObject(SQL_ACCOUNT_FIND_BY_EMAIL, accountRowMapperWithPassword,
            new Object[] { email });
            

            if (!BCrypt.checkpw(password, account.getPassword())) {

                throw new AuthException("Invalid Credentials");
            }
            return account;
            
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException("Invalid Credentials");
        }
    }

    @Override
    public void update(Integer accountId, Account account) {
        try {
            String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt(10));
            jdbcTemplate.update(SQL_ACCOUNT_UPDATE,new Object[] {
                account.getFirstName(),account.getLastName(),account.getEmail(),hashedPassword,accountId
            });
        } catch (Exception e) {
            throw new NotFoundException("Account does not exist.");
        }
    }
}
