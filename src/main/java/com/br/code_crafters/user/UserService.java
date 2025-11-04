package com.br.code_crafters.user;

import com.br.code_crafters.exception.UserAlreadyExistsException;
import com.br.code_crafters.forms.register.RegisterDto;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(OAuth2User principal) {
        String userEmail = principal.getAttribute("email");

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


    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void registerForm(RegisterDto dto){
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("O e-mail informado já está cadastrado.");
        }
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("O nome de usuário escolhido não está disponível.");
        }
        try {
            var user = userMapper(dto);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Função de usuário inválida. Tente novamente.", e);
        }
    }

    public long countUsers(){
        return userRepository.count();
    }

    public User userMapper(RegisterDto dto){
        String roleString = (dto.getUserRole() != null)
                ? String.valueOf(dto.getUserRole())
                : "USER";

        return User.builder()
                .id(dto.getId())
                .name(dto.getNmFull())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .userRole(UserRole.valueOf(roleString))
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }


}

