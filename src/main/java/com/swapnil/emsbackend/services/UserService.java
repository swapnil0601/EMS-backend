package com.swapnil.emsbackend.services;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.models.User;

public interface UserService {
    User getUserById(Integer userId);
    
    User registerUser(String firstName, String lastName, String email, String password, String role) throws AuthException;

    User validateUser(String email, String password) throws AuthException;

    User updateUser(Integer userId, User user) throws AuthException;
}
