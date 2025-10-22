package com.br.code_crafters.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage(Model model){


        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "login";
    }


}
