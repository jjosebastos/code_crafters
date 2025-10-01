package com.br.code_crafters.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class CustomerDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);


    public CustomerDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username) // Assumindo que o 'username' aqui é o email do usuário
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())
        );
        // Para usuários de login de formulário, a senha deve ser a senha codificada.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),       // Principal name (identificador)
                user.getPassword(),    // Senha (vazia para OAuth2, codificada para Form Login)
                authorities            // Roles/Autoridades
        );
    }
}
