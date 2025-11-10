package com.br.code_crafters.forms.ajustes;

import com.br.code_crafters.user.User;
import com.br.code_crafters.user.UserProfile;
import com.br.code_crafters.user.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Controller
@RequestMapping("/ajustes")
public class AjustesController {

    private final AjustesService ajustesService;
    private final MessageSource messageSource;

    public AjustesController(AjustesService ajustesService, MessageSource messageSource) {
        this.ajustesService = ajustesService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String getAjustes(Model model, Authentication authentication) {

        try {
            // O serviço é responsável por extrair o DTO
            AjustesDto dto = ajustesService.getAjustes(authentication);
            model.addAttribute("ajustesDto", dto);
            return "fragments/ajustes"; // Garanta que esta view existe

        } catch (UserPrincipalNotFoundException e) {
            // Lançar exceção para ser tratada por um @ControllerAdvice globalmente
            // ou pelo próprio Spring Security, mas para fins de refatoração, manteremos a exceção.
            // Poderíamos, alternativamente, redirecionar para uma página de erro aqui.
            model.addAttribute("error", "Usuário não encontrado ou não logado.");
            return "redirect:/login";
        }
    }

    /**
     * Salva os ajustes submetidos.
     */
    @PostMapping
    public String postAjustes(@ModelAttribute("ajustesDto") AjustesDto dto,
                              Authentication authentication,
                              RedirectAttributes redirect) {

        try {
            ajustesService.save(dto, authentication);
            redirect.addFlashAttribute("message", "Ajustes salvos com sucesso!");

            // É mais seguro obter o idioma do objeto salvo no serviço,
            // mas mantendo a lógica de uso do DTO:
            redirect.addAttribute("lang", dto.getIdioma());

            return "redirect:/ajustes";

        } catch (UserPrincipalNotFoundException e) {
            redirect.addFlashAttribute("error", "Erro ao salvar: Usuário não encontrado ou não logado.");
            return "redirect:/ajustes";

        } catch (Exception e) {
            // Inclua a mensagem de erro original para debug
            redirect.addFlashAttribute("error", "Erro inesperado ao salvar os ajustes. Detalhe: " + e.getMessage());
            return "redirect:/ajustes";
        }
    }
}
