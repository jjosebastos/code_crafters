package com.br.code_crafters.forms.contact;

import com.br.code_crafters.navigation.BreadcrumbsController;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/contato")
public class ContactController {

    private final MessageSource messageSource;
    private final EmailService emailService;

    public ContactController(MessageSource messageSource, EmailService emailService) {
        this.messageSource = messageSource;
        this.emailService = emailService;
    }

    @GetMapping
    public String showContact(Model model) {
        model.addAttribute("contactDto", new ContactDto());
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/contato";
    }


    @PostMapping
    public String sendMail(@Valid ContactDto dto,
                           BindingResult br,
                           RedirectAttributes redirect) throws MessagingException {
        if(br.hasErrors())
            return "fragments/contato";
        emailService.sendHtmlEmail(dto);
        var message = messageSource.getMessage("email.send.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/contato";
    }

    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedContactBreadcrumb = messageSource.getMessage("contact.breadcrumb", null, currentLocale);
        return List.of(
                new BreadcrumbsController.BreadcrumbItem(localizedContactBreadcrumb, "/contato")
        );
    }
}
