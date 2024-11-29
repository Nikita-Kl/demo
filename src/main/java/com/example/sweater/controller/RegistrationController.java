package com.example.sweater.controller;

import com.example.sweater.domain.User;
import com.example.sweater.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;


@Controller
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @Valid User user,
            BindingResult bindingResult,
            Model model,
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse
    ) {
        return registrationService.registerUser(user, passwordConfirm, captchaResponse, bindingResult, model);
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        return registrationService.activateUser(code, model);
    }
}