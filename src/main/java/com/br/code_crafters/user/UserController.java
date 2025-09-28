package com.br.code_crafters.user;

import com.br.code_crafters.exception.UserAlreadyExistsException;
import com.br.code_crafters.forms.register.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String formRegister(Model model){
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping
    public String saveUser(@Valid RegisterDto dto, BindingResult br, RedirectAttributes redirect, Model model){
        if(br.hasErrors()) return "register";

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            br.rejectValue("passwordConfirm", "error.password", "As senhas não coincidem.");
            return "register";
        }

        try {
            userService.registerForm(dto);
            var message = messageSource.getMessage("user.created.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message);
            return "redirect:/login";

        } catch (UserAlreadyExistsException e) {
            var message = messageSource.getMessage("user.creation.fail", null, LocaleContextHolder.getLocale());
            model.addAttribute("error", message);
            return "register";
        }
    }
}
