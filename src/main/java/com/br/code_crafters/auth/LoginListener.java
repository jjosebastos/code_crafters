package com.br.code_crafters.auth;

import com.br.code_crafters.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService userService;

    public LoginListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        var principal = authentication.getPrincipal();

        if(principal instanceof OAuth2User oauth2User){
            userService.register(oauth2User);
        } else if (principal instanceof UserDetails userDetails) {
            System.out.println("Login bem sucedido. Username: " + userDetails.getUsername());
        }


        log.info("usuário logado " + principal);

    }
}
