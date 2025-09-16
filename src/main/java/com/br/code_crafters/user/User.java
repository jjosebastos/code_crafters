package com.br.code_crafters.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@Table(name = "t_mtu_user")
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Email
    @Column(unique = true)
    private String email;

    private String password;

    public User(OAuth2User principal){
        this.username = principal.getAttributes().get("username").toString();
        this.email = principal.getAttributes().get("email").toString();
        this.email = principal.getAttributes().get("password").toString();
    }

    public User(){

    }
}
