package com.br.code_crafters.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(OAuth2User principal){
        String userEmail = principal.getAttributes().get("email").toString();
        var user = userRepository.findByEmail(userEmail);
        if(user.isEmpty()){
            var newUser = new User(principal);
            newUser.setUsername(userEmail);
            return userRepository.save(newUser);
        }
        return user.get();
    }
}

