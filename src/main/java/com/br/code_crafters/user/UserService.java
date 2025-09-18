package com.br.code_crafters.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(OAuth2User principal) {
        String userEmail = principal.getAttribute("email");

        // Lida com o caso em que o e-mail é nulo, o que é raro, mas possível.
        if (userEmail == null) {
            throw new IllegalArgumentException("Email de usuário não pode ser nulo.");
        }

        var userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(userEmail);

            String userName = principal.getAttribute("name");
            newUser.setName(userName != null ? userName : userEmail);

            newUser.setUsername(userEmail);
            newUser.setPassword("");

            newUser.setUserRole(UserRole.USER);

            return userRepository.save(newUser);
        }

        return userOptional.get();
    }
}

