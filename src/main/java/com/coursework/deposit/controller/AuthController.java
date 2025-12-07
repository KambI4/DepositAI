package com.coursework.deposit.controller;

import com.coursework.deposit.model.RegistrationDto;
import com.coursework.deposit.model.Role;
import com.coursework.deposit.model.UserEntity;
import com.coursework.deposit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") RegistrationDto dto,
                                  Model model) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("user", dto);
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            model.addAttribute("user", dto);
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register";
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

        return "redirect:/login?registered";
    }
}
