package com.swapnil.emsbackend.services.Implementation;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.models.User;
import com.swapnil.emsbackend.repositories.UserRepository;
import com.swapnil.emsbackend.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String role) throws AuthException{
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        if(email != null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches())
            throw new AuthException("Invalid email format");

        try{
            Integer userId = userRepository.create(firstName, lastName, email, password, role);
            return userRepository.findById(userId);          
        }catch(Exception e){
            throw new AuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User validateUser(String email, String password) throws AuthException {
        if(email != null) email = email.toLowerCase();

        try{
            User user = userRepository.findByEmailAndPassword(email, password);
            if(!user.getPassword().equals(password))
                throw new AuthException("Invalid email/password");
            return user;
        }catch(Exception e){
            throw new AuthException("Invalid email/password");
        }
    }

    @Override
    public User updateUser(Integer userId, User user) throws AuthException {
        try{
            userRepository.updateUser(userId, user);
            return userRepository.findById(userId);
        }catch(Exception e){
            throw new AuthException("Invalid details. Failed to update account");
        }
    }
}
