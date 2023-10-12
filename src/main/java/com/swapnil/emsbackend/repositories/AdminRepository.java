package com.swapnil.emsbackend.repositories;

import java.util.List;

import com.swapnil.emsbackend.exceptions.InvalidRequestException;
import com.swapnil.emsbackend.models.User;

public interface AdminRepository {
    void assignAdmin(Integer userId) throws Exception;

    void removeAdmin(Integer userId) throws Exception;

    List<User> getAllAdmins() throws InvalidRequestException;
}
