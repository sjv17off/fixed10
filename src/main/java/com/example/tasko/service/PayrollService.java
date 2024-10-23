package com.example.tasko.service;

import com.example.tasko.model.Attendance;
import com.example.tasko.model.Payroll;
import com.example.tasko.model.User;
import com.example.tasko.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Transactional
    public Payroll generatePayroll(User user, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceService.getUserAttendance(user, startDate, endDate);
        
        Payroll payroll = new Payroll();
        payroll.setUser(user);
        payroll.setPayPeriodStart(startDate);
        payroll.setPayPeriodEnd(endDate);
        
        // Calculate basic salary based on attendance
        BigDecimal basicSalary = calculateBasicSalary(user, attendances);
        payroll.setBasicSalary(basicSalary);
        
        // Calculate overtime if any
        BigDecimal overtime = calculateOvertime(attendances);
        payroll.setOvertime(overtime);
        
        // Calculate deductions based on late days/absences
        BigDecimal deductions = calculateDeductions(attendances);
        payroll.setDeductions(deductions);
        
        // Calculate net salary
        BigDecimal netSalary = basicSalary.add(overtime).subtract(deductions);
        payroll.setNetSalary(netSalary);
        
        return payrollRepository.save(payroll);
    }

    private BigDecimal calculateBasicSalary(User user, List<Attendance> attendances) {
        // Implementation for basic salary calculation
        return BigDecimal.ZERO; // Placeholder
    }

    private BigDecimal calculateOvertime(List<Attendance> attendances) {
        // Implementation for overtime calculation
        return BigDecimal.ZERO; // Placeholder
    }

    private BigDecimal calculateDeductions(List<Attendance> attendances) {
        // Implementation for deductions calculation
        return BigDecimal.ZERO; // Placeholder
    }

    @Transactional(readOnly = true)
    public List<Payroll> getUserPayroll(User user) {
        return payrollRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Payroll> getPendingPayrolls() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        return payrollRepository.findByPayPeriodStartBetweenAndPaidFalse(
            startOfMonth.minusMonths(1), 
            startOfMonth.plusMonths(1)
        );
    }

    @Transactional
    public Payroll processPayment(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
            .orElseThrow(() -> new RuntimeException("Payroll not found"));
        payroll.setPaid(true);
        payroll.setPaymentDate(LocalDate.now());
        return payrollRepository.save(payroll);
    }
}