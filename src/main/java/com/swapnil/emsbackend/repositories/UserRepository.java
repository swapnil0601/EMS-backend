package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.exceptions.NotFound;
import com.swapnil.emsbackend.models.User;

public interface UserRepository {
    Integer create(String firstName, String lastName, String email, String password, String role) throws AuthException;

    User findById(Integer userId) throws NotFound;

    User findByEmailAndPassword(String email,String password) throws AuthException;

    void updateUser(Integer userId,User user) throws NotFound;
}
