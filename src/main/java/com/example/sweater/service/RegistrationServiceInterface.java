package com.example.sweater.service;

import com.example.sweater.domain.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface RegistrationServiceInterface {

    String registerUser(User user, String passwordConfirm, String captchaResponse,
                        BindingResult bindingResult, Model model);

    String activateUser(String code, Model model);
}
