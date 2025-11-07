package com.br.code_crafters.forms.ajustes;

import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserProfile;
import com.br.code_crafters.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@Service
public class AjustesService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public AjustesService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public AjustesDto getAjustes(Authentication authentication) throws UserPrincipalNotFoundException {

        String emailUsuario;
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            emailUsuario = ((OAuth2User) principal).getAttribute("email");
            if (emailUsuario == null) {
                throw new UserPrincipalNotFoundException("Email não encontrado no token OAuth2");
            }
        } else if (principal instanceof UserDetails) {
            emailUsuario = ((UserDetails) principal).getUsername();
        } else {
            // Cenário 3: Tipo de principal desconhecido
            throw new UserPrincipalNotFoundException("Tipo de principal de usuário desconhecido: " + principal.getClass().getName());
        }

        // O resto da sua lógica de negócios permanece o mesmo
        User user = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserPrincipalNotFoundException("Usuário não encontrado no banco com email: " + emailUsuario));
        UserProfile profile = user.getUserProfile();
        return mapToDto(user, Objects.requireNonNullElseGet(profile, UserProfile::new));
    }
    @Transactional
    public void save(AjustesDto dto, Authentication authentication) throws UserPrincipalNotFoundException {
        String emailUsuario = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        if (emailUsuario == null) throw new UserPrincipalNotFoundException("Email não encontrado no token OAuth2");
        User user = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserPrincipalNotFoundException("Usuário não encontrado no banco com email: " + emailUsuario));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }
        user.setName(dto.getNome());
        mapToEntity(dto, profile);
        userRepository.save(user);
        userProfileRepository.save(profile);
    }

    private AjustesDto mapToDto(User user, UserProfile profile) {
        AjustesDto dto = new AjustesDto();
        dto.setNome(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSobrenome(user.getName());
        dto.setBio(profile.getBio());
        dto.setTelefone(profile.getTelefone());
        dto.setTema(profile.getTema());
        dto.setIdioma(profile.getIdioma());
        dto.setGenero(profile.getGenero());
        dto.setNotificacoesAtivas(profile.isNotificacoesAtivas());
        return dto;
    }

    private void mapToEntity(AjustesDto dto, UserProfile profile) {
        profile.setSobrenome(dto.getSobrenome());
        profile.setBio(dto.getBio());
        profile.setTelefone(dto.getTelefone());
        profile.setTema(dto.getTema());
        profile.setIdioma(dto.getIdioma());
        profile.setGenero(dto.getGenero());
        profile.setNotificacoesAtivas(dto.isNotificacoesAtivas());
    }
}
