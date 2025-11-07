package com.br.code_crafters.forms.monitoring;


import com.br.code_crafters.navigation.BreadcrumbsController;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Locale;

@Controller
public class RastreamentoController {
    private final MessageSource messageSource;

    public RastreamentoController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/rastreamento")
    public String rastreamento(Model model, Locale locale) {
        model.addAttribute("breadcrumb", List.of(
                new BreadcrumbsController.BreadcrumbItem(messageSource.getMessage("layout.aside.tracking", null, locale), null)
        ));

        return "fragments/rastreamento";
    }
}
