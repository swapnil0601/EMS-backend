package com.swapnil.emsbackend.repositories.Implementations;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.exceptions.NotFound;
import com.swapnil.emsbackend.models.User;
import com.swapnil.emsbackend.repositories.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_USER_CREATE = "INSERT INTO USER (FIRSTNAME,LASTNAME , EMAIL , PASSWORD , ROLE) VALUES(?,?,?,?,?)";

    private static final String SQL_USER_FIND_BY_ID = "SELECT USERID,FIRSTNAME,LASTNAME , EMAIL , PASSWORD , ROLE"+ "FROM USER" + "WHERE USERID = ?";

    private static final String
    SQL_USER_FIND_BY_EMAIL 
    = "SELECT USERID , FIRSTNAME , LASTNAME , EMAIL , PASSWORD , ROLE " +
            "FROM USER " +
            "WHERE EMAIL = ?";

    private static final String SQL_USER_UPDATE =
    "UPDATE USER SET FIRSTNAME=? , LASTNAME=? , EMAIL=? , PASSWORD =? WHERE USERID=?;";

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(
                rs.getInt("USERID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("ROLE"));
    });

    private RowMapper<User> userRowMapperWithPassword = ((rs, rowNum) -> {
        return new User(
                rs.getInt("USERID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("ROLE"));
    });

    @Override
    public Integer create(String firstName, String lastName, String email, String password, String role) throws AuthException {
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_USER_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashPassword);
                ps.setString(5, role);
                return ps;
            }, keyHolder);

            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            throw new AuthException("Invalid Details");
        }
    }

    @Override
    public User findById(Integer userId){
        return jdbcTemplate.queryForObject(SQL_USER_FIND_BY_ID, userRowMapper, new Object[] { userId });
    }
    
    @Override
    public User findByEmailAndPassword(String email, String password) throws AuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_USER_FIND_BY_EMAIL, userRowMapperWithPassword,
                    new Object[] { email });

            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthException("Invalid Credentials");
            }
            return user;

        } catch (EmptyResultDataAccessException e) {
            throw new AuthException("Invalid Credentials");
        }
    }

    @Override
    public void updateUser(Integer userId, User user) {
        try {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
            jdbcTemplate.update(SQL_USER_UPDATE,new Object[] {
                user.getFirstName(),user.getLastName(),user.getEmail(),hashedPassword,userId
            });
        } catch (Exception e) {
            throw new NotFound("User does not exist.");
        }
    }
}
