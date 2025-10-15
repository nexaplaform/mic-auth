package com.nexaplatform.api.controllers;

import com.nexaplatform.api.services.dto.in.UserDtoIn;
import com.nexaplatform.api.services.mappers.UserDtoMapper;
import com.nexaplatform.application.useccase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserDtoMapper mapper;
    private final UserUseCase userUseCase;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new UserDtoIn());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerForm") UserDtoIn form, Model model) {

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }

//        User user = userUseCase.findUserByEmail(form.getEmail());
//
//        if (Objects.nonNull(user)) {
//            model.addAttribute("error", "El correo ya está registrado");
//            return "register";
//        }

        mapper.toDto(userUseCase.create(mapper.toDomain(form)));
        return "redirect:/login?registered";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }
}
