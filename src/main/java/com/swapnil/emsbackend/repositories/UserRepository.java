package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.exceptions.NotFoundException;
import com.swapnil.emsbackend.models.User;

public interface UserRepository {
    Integer create(String firstName, String lastName, String email, String password, String role) throws AuthException;

    User findById(Integer userId) throws NotFoundException;

    User findByEmailAndPassword(String email,String password) throws AuthException;

    User findByEmail(String email) throws AuthException;

    void updateUser(Integer userId,User user) throws NotFoundException;
}
