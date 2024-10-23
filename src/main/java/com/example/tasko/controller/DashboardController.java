package com.example.tasko.controller;

import com.example.tasko.model.*;
import com.example.tasko.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName());
        Enterprise enterprise = user.getEnterprise();
        model.addAttribute("enterprise", enterprise);

        if (user.getRole() == UserRole.ADMIN) {
            return setupAdminDashboard(model, enterprise);
        } else {
            return setupUserDashboard(model, user);
        }
    }

    private String setupAdminDashboard(Model model, Enterprise enterprise) {
        // Add statistics
        model.addAttribute("userCount", userService.countUsersByEnterprise(enterprise));
        model.addAttribute("activeTaskCount", taskService.countActiveTasksByEnterprise(enterprise));
        model.addAttribute("todayAttendanceCount", attendanceService.countTodayAttendanceByEnterprise(enterprise));

        // Add recent activities
        model.addAttribute("recentTasks", taskService.getRecentTasksByEnterprise(enterprise));
        model.addAttribute("recentAttendance", attendanceService.getRecentAttendanceByEnterprise(enterprise));

        return "dashboard/admin";
    }

    private String setupUserDashboard(Model model, User user) {
        // Get today's attendance
        Attendance todayAttendance = attendanceService.getTodayAttendance(user);
        model.addAttribute("todayAttendance", todayAttendance);

        // Get user's tasks
        List<Task> recentTasks = taskService.getRecentTasksByUser(user);
        model.addAttribute("recentTasks", recentTasks);

        return "dashboard/user";
    }
}