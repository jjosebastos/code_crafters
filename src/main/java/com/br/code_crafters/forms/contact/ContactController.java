package com.br.code_crafters.forms.contact;

import com.br.code_crafters.navigation.BreadcrumbsController;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Locale;

@Controller
public class ContactController {

    private final MessageSource messageSource;

    public ContactController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/contato")
    public String showContact(Model model) {
        List<BreadcrumbsController.BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbsController.BreadcrumbItem("Contato", "/contato")
        );
        model.addAttribute("breadcrumb", createBreadcrumb());
        model.addAttribute("pageTitleKey", "pageTitle.contact");
        return "fragments/contato";
    }

    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedContactBreadcrumb = messageSource.getMessage("contact.breadcrumb", null, currentLocale);
        return List.of(
                new BreadcrumbsController.BreadcrumbItem(localizedContactBreadcrumb, "/contato")
        );
    }
}
