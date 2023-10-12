package com.swapnil.emsbackend.repositories.Implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.User;
import com.swapnil.emsbackend.repositories.AdminRepository;

@Repository
public class AdminRepositoryImpl implements AdminRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ASSIGN_ADMIN = "UPDATE USER SET is_admin = true WHERE employee_id = ?";

    private static final String SQL_REMOVE_ADMIN = "UPDATE USER SET is_admin = false WHERE employee_id = ?";

    private static final String SQL_GET_ALL_ADMINS = "SELECT * FROM USER WHERE is_admin = true";

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(
            rs.getInt("user_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("is_admin")
        );
    }); 

    @Override
    public void assignAdmin(Integer userId) throws Exception {
        try {
            jdbcTemplate.update(SQL_ASSIGN_ADMIN, userId);
        } catch (Exception e) {
            throw new Exception("Error assigning admin");
        }
    }

    @Override
    public void removeAdmin(Integer userId) throws Exception {
        try {
            jdbcTemplate.update(SQL_REMOVE_ADMIN, userId);
        } catch (Exception e) {
            throw new Exception("Error removing admin");
        }
    }

    @Override
    public List<User> getAllAdmins() throws InvalidRequestException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL_ADMINS, userRowMapper, new Object[]{});
        } catch (Exception e) {
            throw new InvalidRequestException("Error getting all admins");
        }
    }
}
