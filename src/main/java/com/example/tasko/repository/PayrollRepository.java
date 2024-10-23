package com.example.tasko.repository;

import com.example.tasko.model.Payroll;
import com.example.tasko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByUser(User user);
    List<Payroll> findByUserAndPayPeriodStartBetween(User user, LocalDate start, LocalDate end);
    List<Payroll> findByPayPeriodStartBetweenAndPaidFalse(LocalDate start, LocalDate end);
}