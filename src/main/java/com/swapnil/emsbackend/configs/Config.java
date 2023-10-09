package com.swapnil.emsbackend.configs;

import com.swapnil.emsbackend.models.Admin;
import com.swapnil.emsbackend.models.Employee;
import com.swapnil.emsbackend.models.Record;
import com.swapnil.emsbackend.repositories.AdminRepository;
import com.swapnil.emsbackend.repositories.EmployeeRepository;
import com.swapnil.emsbackend.repositories.RecordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository, AdminRepository adminRepository, RecordRepository recordRepository){
        return args -> {
            Admin kunal = new Admin(
                    "Kunal Agrawal",
                    "kunalagrawal@gmail.com",
                    "1234"
            );
            adminRepository.saveAll(List.of(kunal));

            Employee swapnil = new Employee(
                    "Swapnil Sahoo",
                    "swapnilsahoo100@gmail.com",
                    "1234"
            );
            Employee ashutosh = new Employee(
                    "Ashutosh Padhy",
                    "a.padhy@gmail.com",
                    "2345"
            );
            employeeRepository.saveAll(List.of(swapnil,ashutosh));

            Record swapnilDay1 = new Record(swapnil, LocalDate.now(),true,false,true);
            Record swapnilDay0 = new Record(swapnil,LocalDate.now().minusDays(1),true,true,false);
            Record swapnilDay2 = new Record(swapnil,LocalDate.now().minusDays(3),true,true,false);

            Record ashuDay0 = new Record(ashutosh,LocalDate.now().minusDays(2),false,false,false);
            Record ashuDay1 = new Record(ashutosh,LocalDate.now().minusDays(1),true,true,false);

            recordRepository.saveAll(List.of(swapnilDay0,swapnilDay1,swapnilDay2,ashuDay0,ashuDay1));
        };
    }
}
