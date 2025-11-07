package com.br.code_crafters.config;


import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserProfile;
import com.br.code_crafters.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributesAdvice {

    private final UserRepository userRepository;

    public GlobalModelAttributesAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("globalFotoUrl")
    @Transactional(readOnly = true)
    public String addGlobalFotoUrl(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof OidcUser) {
            email = ((OidcUser) principal).getAttribute("email");
        } else if (principal instanceof OAuth2User) {
            email = ((OAuth2User) principal).getAttribute("email");
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        if (email != null) {

            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {

                UserProfile profile = user.getUserProfile();
                if (profile != null) {
                    return profile.getFotoUrl();
                }
            }
        }

        return null;
    }
}