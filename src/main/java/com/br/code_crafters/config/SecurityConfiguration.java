package com.br.code_crafters.config;

import com.br.code_crafters.user.CustomerDetailsService;
import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // REMOVIDO: O bean AuthenticationManager, pois ele estava sobrescrevendo o auto-configurado.
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }


    // --- 5. SecurityFilterChain: A configuração principal ---
    // REMOVIDO: AuthenticationManager da assinatura do método
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REMOVIDO: A chamada .authenticationManager(authenticationManager) para permitir auto-configuração
                // .authenticationManager(authenticationManager) 

                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Recommended for H2 Console
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Recommended for H2 Console
                .authorizeHttpRequests(auth -> auth
                        // Permitted public endpoints
                        .requestMatchers(
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**",
                                "/login", "/register", "/form", "/login?error=true", "/error"
                        ).permitAll()

                        // RESTRICTED ENDPOINTS (ADMIN)
                        .requestMatchers(HttpMethod.DELETE,
                                "/filiais/{uuid}", "/motos/{uuid}", "/operadores/{uuid}", "/patios/{uuid}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/filiais/{uuid}", "/motos/{uuid}", "/operadores/{uuid}", "/patios/{uuid}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/filiais/{uuid}", "/motos/{uuid}", "/operadores/{uuid}", "/patios/{uuid}")
                        .hasRole("ADMIN")

                        // RESTRICTED ENDPOINTS (USER/ADMIN)
                        .requestMatchers(HttpMethod.GET,
                                "/filiais", "/filiais/form", "/filiais/{uuid}",
                                "/motos", "/motos/form", "/motos/{uuid}",
                                "/operadores", "/operadores/form", "/operadores/{uuid}",
                                "/patios", "/patios/form", "/patios/{uuid}")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/filiais", "/motos", "/operadores", "/patios")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/monitoramento").hasAnyRole("USER", "ADMIN")

                        // Authenticated required for dashboard and root
                        .requestMatchers("/dashboard", "/").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        // Acesso a /login é permitido no authorizeHttpRequests acima
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())
                                .userService(this.oauth2UserService())
                        )
                )
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
        final OidcUserService delegate = new OidcUserService();
        return new OidcUserService() {
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
