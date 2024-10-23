package com.example.tasko.controller;

import com.example.tasko.model.Enterprise;
import com.example.tasko.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/enterprises")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping
    public String listEnterprises(Model model) {
        model.addAttribute("enterprises", enterpriseService.getAllEnterprises());
        return "enterprises/list";
    }

    @GetMapping("/create")
    public String createEnterpriseForm(Model model) {
        model.addAttribute("enterprise", new Enterprise());
        return "enterprises/create";
    }

    @PostMapping("/create")
    public String createEnterprise(@Valid @ModelAttribute Enterprise enterprise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "enterprises/create";
        }
        enterpriseService.createEnterprise(enterprise);
        return "redirect:/enterprises";
    }

    @GetMapping("/edit/{id}")
    public String editEnterpriseForm(@PathVariable Long id, Model model) {
        Enterprise enterprise = enterpriseService.getEnterpriseById(id);
        if (enterprise != null) {
            model.addAttribute("enterprise", enterprise);
            return "enterprises/edit";
        }
        return "redirect:/enterprises";
    }

    @PostMapping("/edit/{id}")
    public String editEnterprise(@PathVariable Long id, @Valid @ModelAttribute Enterprise enterprise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "enterprises/edit";
        }
        enterprise.setId(id);
        enterpriseService.updateEnterprise(enterprise);
        return "redirect:/enterprises";
    }

    @GetMapping("/delete/{id}")
    public String deleteEnterprise(@PathVariable Long id) {
        enterpriseService.deleteEnterprise(id);
        return "redirect:/enterprises";
    }
}