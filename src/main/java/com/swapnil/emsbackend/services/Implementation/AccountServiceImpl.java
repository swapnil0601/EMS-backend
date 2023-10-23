package com.swapnil.emsbackend.services.Implementation;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swapnil.emsbackend.exceptions.AuthException;
import com.swapnil.emsbackend.models.Account;
import com.swapnil.emsbackend.repositories.AccountRepository;
import com.swapnil.emsbackend.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public Account register(String firstName, String lastName, String email, String password, String role) throws AuthException{

        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        if(email != null) {
            email = email.toLowerCase();
            if (!pattern.matcher(email).matches())
                throw new AuthException("Invalid email format");
        }
        try{
            return accountRepository.create(firstName, lastName, email, password, role); 
        }catch(Exception e){
            throw new AuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public Account login(String email, String password) throws AuthException {
        if(email != null) email = email.toLowerCase();

        try{
            Account account = accountRepository.findByEmailAndPassword(email, password);
            return account;
        }catch(Exception e){
            throw new AuthException("Invalid email/password");
        }
    }

    @Override
    public Account update(Integer accountId, Account account) throws AuthException {
        try{
            accountRepository.update(accountId, account);
            return accountRepository.findById(accountId);
        }catch(Exception e){
            throw new AuthException("Invalid details. Failed to update account");
        }
    }
}
