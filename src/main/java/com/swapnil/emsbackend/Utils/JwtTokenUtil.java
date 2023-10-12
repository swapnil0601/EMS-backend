package com.swapnil.emsbackend.Utils;

import java.util.Date;

import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.models.Employee;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private static final String SECRET_KEY="jwtsecrettoken";

    public String generateJwtToken(Account account,Employee employee)
    {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256),SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime+30*60*1000))
                .claim("accountId", account.getAccountId())
                .claim("email",account.getEmail()
                ).claim("firstName",account.getFirstName())
                .claim("lastName",account.getLastName())
                .claim("role",account.getRole())
                .claim("employee",employee.getEmployeeId()).compact();
    }

    public boolean validateJwtToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS256)).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            return false;
        }
    }
}
