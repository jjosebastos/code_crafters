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
    public String getAjustes(Model model,
                             RedirectAttributes redirect,
                             Authentication authentication) {

        try {
            AjustesDto dto = ajustesService.getAjustes(authentication);
            model.addAttribute("ajustesDto", dto);
            return "fragments/ajustes";

        } catch (UserPrincipalNotFoundException e) {
            redirect.addFlashAttribute("error", "Usuário não encontrado: " + e.getMessage());
            return "redirect:/index";
        }
    }

    @PostMapping
    public String postAjustes(@ModelAttribute("ajustesDto") AjustesDto dto,
                              Authentication authentication,
                              RedirectAttributes redirect) {

        try {
            ajustesService.save(dto, authentication);
            redirect.addFlashAttribute("message", "Ajustes salvos com sucesso!");
            return "redirect:/ajustes";

        } catch (UserPrincipalNotFoundException e) {
            redirect.addFlashAttribute("error", "Erro ao salvar: Usuário não encontrado.");
            return "redirect:/ajustes";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Erro ao salvar: " + e.getMessage());
            return "redirect:/ajustes";
        }
    }
}
