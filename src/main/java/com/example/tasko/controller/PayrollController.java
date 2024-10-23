package com.example.tasko.controller;

import com.example.tasko.model.Payroll;
import com.example.tasko.model.User;
import com.example.tasko.service.PayrollService;
import com.example.tasko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private UserService userService;

    @GetMapping("/manage")
    public String managePendingPayrolls(Model model) {
        List<Payroll> pendingPayrolls = payrollService.getPendingPayrolls();
        model.addAttribute("payrolls", pendingPayrolls);
        return "payroll/manage";
    }

    @GetMapping("/my-payroll")
    public String viewMyPayroll(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName());
        List<Payroll> payrolls = payrollService.getUserPayroll(user);
        model.addAttribute("payrolls", payrolls);
        return "payroll/view";
    }

    @PostMapping("/generate")
    public String generatePayroll(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        User user = userService.getUserById(userId);
        payrollService.generatePayroll(user, startDate, endDate);
        return "redirect:/payroll/manage";
    }

    @PostMapping("/process/{id}")
    public String processPayment(@PathVariable Long id) {
        payrollService.processPayment(id);
        return "redirect:/payroll/manage";
    }

    @GetMapping("/reports")
    public String payrollReports(Model model) {
        // Add logic for payroll reports
        return "payroll/reports";
    }
}