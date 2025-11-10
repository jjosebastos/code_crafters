package com.br.code_crafters.forms.ajustes;

import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserProfile;
import com.br.code_crafters.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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

    private String getPrincipalEmail(Authentication authentication) throws UserPrincipalNotFoundException {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserPrincipalNotFoundException("Usuário não autenticado.");
        }

        String emailUsuario;
        Object principal = authentication.getPrincipal();
        if (principal instanceof OidcUser) { // OidcUser é mais específico e deve vir primeiro
            emailUsuario = ((OidcUser) principal).getAttribute("email");
        } else if (principal instanceof OAuth2User) {
            emailUsuario = ((OAuth2User) principal).getAttribute("email");
        }
        else if (principal instanceof UserDetails) {
            emailUsuario = ((UserDetails) principal).getUsername();
        }
        else {
            throw new UserPrincipalNotFoundException("Tipo de principal de usuário desconhecido: " + principal.getClass().getName());
        }

        if (emailUsuario == null || emailUsuario.isEmpty()) {
            throw new UserPrincipalNotFoundException("Email não encontrado no objeto Principal após a extração.");
        }

        return emailUsuario;
    }

    // =========================================================================
    // GET AJUSTES (Mantido, mas usando o método auxiliar)
    // =========================================================================
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public AjustesDto getAjustes(Authentication authentication) throws UserPrincipalNotFoundException {

        // ✅ Uso seguro
        String emailUsuario = getPrincipalEmail(authentication);

        User user = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserPrincipalNotFoundException("Usuário não encontrado no banco com email: " + emailUsuario));

        UserProfile profile = user.getUserProfile();
        return mapToDto(user, Objects.requireNonNullElseGet(profile, UserProfile::new));
    }

    // =========================================================================
    // SAVE (CORRIGIDO)
    // =========================================================================
    @Transactional
    public void save(AjustesDto dto, Authentication authentication) throws UserPrincipalNotFoundException {

        // ✅ CORREÇÃO APLICADA: Substituído o cast inseguro pela extração segura
        String emailUsuario = getPrincipalEmail(authentication);

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
        // Implementação do mapToDto... (Mantida)
        AjustesDto dto = new AjustesDto();
        dto.setNome(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSobrenome(profile.getSobrenome()); // Assumindo que Sobrenome está no Profile agora
        dto.setBio(profile.getBio());
        dto.setTelefone(profile.getTelefone());
        dto.setTema(profile.getTema());
        dto.setIdioma(profile.getIdioma());
        dto.setGenero(profile.getGenero());
        dto.setNotificacoesAtivas(profile.isNotificacoesAtivas());
        return dto;
    }

    private void mapToEntity(AjustesDto dto, UserProfile profile) {
        // Implementação do mapToEntity... (Mantida)
        profile.setSobrenome(dto.getSobrenome());
        profile.setBio(dto.getBio());
        profile.setTelefone(dto.getTelefone());
        profile.setTema(dto.getTema());
        profile.setIdioma(dto.getIdioma());
        profile.setGenero(dto.getGenero());
        profile.setNotificacoesAtivas(dto.isNotificacoesAtivas());
    }
}
