package com.br.code_crafters.config;

import com.br.code_crafters.user.CustomerDetailsService;
import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final CustomerDetailsService customerDetailsService;
    private final UserRepository userRepository;

    public SecurityConfiguration(CustomerDetailsService customerDetailsService, UserRepository userRepository) {
        this.customerDetailsService = customerDetailsService;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customerDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ... outras configurações ...
                .authorizeHttpRequests(auth -> auth
                        // 1. Permissões Públicas
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**").permitAll()
                        .requestMatchers("/login", "/register", "/form").permitAll()

                        // 2. Rotas para PUT e DELETE (as mais restritivas, para ADMIN, vêm PRIMEIRO)
                        // IMPORTANTE: Adicione HttpMethod.POST para URLs com {uuid} aqui também!
                        // FILIAIS
                        .requestMatchers(HttpMethod.DELETE, "/filiais/{uuid}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/filiais/{uuid}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/filiais/{uuid}").hasRole("ADMIN") // <<< Adicionado!

                        // MOTOS
                        .requestMatchers(HttpMethod.DELETE, "/motos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/motos/{uuid}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/motos/{uuid}").hasRole("ADMIN") // <<< Adicionado!

                        // OPERADORES
                        .requestMatchers(HttpMethod.DELETE, "/operadores/{uuid}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/operadores/{uuid}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/operadores/{uuid}").hasRole("ADMIN") // <<< Adicionado!

                        // PÁTIOS
                        .requestMatchers(HttpMethod.DELETE, "/patios/{uuid}").hasRole("ADMIN") // Sua regra DELETE
                        .requestMatchers(HttpMethod.PUT, "/patios/{uuid}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/patios/{uuid}").hasRole("ADMIN") // <<< ESSA É A CHAVE para o seu POST!

                        // 3. Rotas para GET e POST (para USER e ADMIN) - APENAS para POST de CRIAÇÃO!
                        // Note que POST para criar (sem {uuid}) ainda é permitido para USER.
                        .requestMatchers(HttpMethod.GET, "/filiais", "/filiais/form", "/filiais/{uuid}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/filiais").hasAnyRole("USER", "ADMIN") // POST de criação (sem UUID na URL)

                        .requestMatchers(HttpMethod.GET, "/motos", "/motos/form", "/motos/{uuid}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/motos").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/operadores", "/operadores/form", "/operadores/{uuid}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/operadores").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/patios", "/patios/form", "/patios/{uuid}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/patios").hasAnyRole("USER", "ADMIN")

                        // MONITORAMENTO
                        .requestMatchers(HttpMethod.GET, "/monitoramento").hasAnyRole("USER", "ADMIN")

                        // 4. Rotas Autenticadas Gerais
                        .requestMatchers("/dashboard", "/").authenticated()

                        // 5. Catch-all
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())   // Customização para OIDC
                                .userService(this.oauth2UserService())     // Customização para OAuth2 genérico
                        ).permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );


        return http.build();
    }

    @Bean
    public OidcUserService oidcUserService() {
        final OidcUserService delegate = new OidcUserService(); // Mantém o delegate padrão
        return new OidcUserService() { // Criar uma instância anônima
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) {
                OidcUser oidcUser = delegate.loadUser(userRequest);
                Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());

                String email = oidcUser.getEmail();
                if (email != null) {
                    Optional<User> userOptional = userRepository.findByEmail(email);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            }
        };
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (userRequest) -> {
            OAuth2User oauth2User = delegate.loadUser(userRequest);
            Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());

            String email = oauth2User.getAttribute("email");
            if (email != null) {
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            return new DefaultOAuth2User(authorities, oauth2User.getAttributes(), "name");
        };
    }
}