package com.medilabo.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    //Page de connexion
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}