package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

    @Query("Select e from Employee e WHERE e.email= ?1")
    Optional<Admin> findAdminByEmail(String email);
}
