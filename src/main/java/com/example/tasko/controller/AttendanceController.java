package com.example.tasko.controller;

import com.example.tasko.model.Attendance;
import com.example.tasko.model.User;
import com.example.tasko.service.AttendanceService;
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
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @GetMapping("/log")
    public String logAttendanceForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        return "attendance/log";
    }

    @PostMapping("/log")
    public String logAttendance(@ModelAttribute Attendance attendance, Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        attendance.setUser(user);
        attendance.setDate(LocalDate.now());
        attendanceService.logAttendance(attendance);
        return "redirect:/dashboard";
    }

    @GetMapping("/view")
    public String viewAttendance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication,
            Model model) {
        User user = userService.getUserByUsername(authentication.getName());
        List<Attendance> attendanceList = attendanceService.getUserAttendance(user, startDate, endDate);
        model.addAttribute("attendanceList", attendanceList);
        return "attendance/view";
    }

    @GetMapping("/calendar")
    public String viewAttendanceCalendar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month,
            Model model) {
        if (month == null) {
            month = LocalDate.now().withDayOfMonth(1);
        }
        List<Attendance> attendanceList = attendanceService.getMonthlyAttendance(month);
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("currentMonth", month);
        return "attendance/calendar";
    }
}