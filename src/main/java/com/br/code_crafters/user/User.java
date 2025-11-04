package com.br.code_crafters.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Table(name = "t_mtu_user")
@Entity
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Email
    @Column(unique = true)
    private String email;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private UserProfile userProfile;
    public void addUserProfile(UserProfile profile) {
        this.userProfile = profile;
        if (profile != null) {
            profile.setUser(this);
        }
    }
    public void removeUserProfile() {
        if (this.userProfile != null) {
            this.userProfile.setUser(null);
            this.userProfile = null;
        }
    }

    public User(OAuth2User principal){
        this.email = principal.getAttributes().get("email").toString();
        this.name = principal.getAttributes().get("name").toString();
    }

    public User(){

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.toString()));
    }
}
