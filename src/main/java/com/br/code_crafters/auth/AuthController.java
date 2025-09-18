package com.br.code_crafters.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "index";
    }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

}
