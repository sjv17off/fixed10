package com.example.tasko.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/404")
    public String handleNotFound() {
        return "error/404";
    }

    @GetMapping("/error/500")
    public String handleServerError() {
        return "error/500";
    }
}