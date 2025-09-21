package com.br.code_crafters.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping
    public String registerPageUi(){
        return "register-ui";
    }


}
