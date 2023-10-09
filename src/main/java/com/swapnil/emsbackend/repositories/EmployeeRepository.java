package com.swapnil.emsbackend.repositories;

import com.swapnil.emsbackend.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("Select e from Employee e WHERE e.email= ?1")
    Optional<Employee> findEmployeeByEmail(String email);
}
