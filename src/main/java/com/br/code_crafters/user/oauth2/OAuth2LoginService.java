package com.br.code_crafters.user.oauth2;

import com.br.code_crafters.forms.ajustes.UserProfileRepository;
import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserProfile;
import com.br.code_crafters.user.UserRepository;
import com.br.code_crafters.user.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2LoginService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public OAuth2LoginService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }


    @Transactional
    public Set<GrantedAuthority> processOAuth2User(String email, String name, String pictureUrl, String familyName, String provider) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()));
            UserProfile profile = user.getUserProfile();
            boolean needsSave = false;

            if (profile == null) {
                // Usuário existe, mas não tem perfil. Crie um.
                profile = new UserProfile();
                profile.setUser(user); // Linka ao usuário existente
                needsSave = true;
            }

            if (profile.getFotoUrl() == null && pictureUrl != null) {
                profile.setFotoUrl(pictureUrl);
                needsSave = true;
            }
            if (profile.getSobrenome() == null) {
                profile.setSobrenome(familyName != null ? familyName : name);
                needsSave = true;
            }

            if (needsSave) {
                userProfileRepository.save(profile);
            }

        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setUserRole(UserRole.USER);
            User savedUser = userRepository.save(newUser); // <-- SALVE #1 (User)

            UserProfile newProfile = new UserProfile();
            newProfile.setSobrenome(familyName != null ? familyName : name);
            newProfile.setFotoUrl(pictureUrl);
            newProfile.setUser(savedUser);

            userProfileRepository.save(newProfile);
        }

        return authorities;
    }
}