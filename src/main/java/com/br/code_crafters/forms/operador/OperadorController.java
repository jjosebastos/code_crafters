package com.br.code_crafters.forms.operador;


import com.br.code_crafters.exception.CpfAlreadyExistsException;
import com.br.code_crafters.navigation.BreadcrumbsController;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/operadores")
public class OperadorController {


    private final OperadorService operadorService;
    private final MessageSource messageSource;

    public OperadorController(OperadorService operadorService, MessageSource messageSource) {
        this.operadorService = operadorService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showAll(@RequestParam(required = false) String search,
                          @PageableDefault(size = 10, sort = "nome") Pageable pageable,
                          Model model) {

        Page<Operador> operadoresPage;
        if (search != null && !search.trim().isEmpty()) {
            operadoresPage = operadorService.findByNomeOrCpf(search, search, pageable);
        } else {
            operadoresPage = operadorService.findAll(pageable);
        }

        model.addAttribute("operadoresPage", operadoresPage);
        model.addAttribute("breadcrumb", createBreadcrumb());

        if (search != null) {
            model.addAttribute("search", search);
        }

        return "fragments/operadores/listagem";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) UUID uuid, Model model) {
        if (uuid != null) {
            Operador operador = operadorService.findById(uuid)
                    .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado com o ID: " + uuid));
            model.addAttribute("operadorDto", operador);
        } else {
            model.addAttribute("operadorDto", new OperadorDto());
            model.addAttribute("pageTitleKey", "operador.form.title.create");
        }
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/operadores/formOperadores";
    }

    @PostMapping
    public String postOperadores(@Valid @ModelAttribute("operadorDto") OperadorDto dto, BindingResult br, RedirectAttributes redirect) {
        if (br.hasErrors()) {
            return "fragments/operadores/formOperadores";
        }

        try {
            operadorService.save(dto);
            var message = messageSource.getMessage("operador.create.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message);
            return "redirect:/operadores";
        } catch (CpfAlreadyExistsException e) {

            String errorMessage = messageSource.getMessage("operador.cpf.unique", null, LocaleContextHolder.getLocale());
            FieldError cpfError = new FieldError("operadorDto", "cpf", dto.getCpf(), false, null, null, errorMessage);
            br.addError(cpfError);

            return "fragments/operadores/formOperadores";
        }
    }

    @PutMapping("/{uuid}")
    public String update(@PathVariable UUID uuid,
                         @Valid @ModelAttribute("operadorDto") OperadorDto operadorDto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "fragments/operadores/formOperadores";
        }
        operadorService.update(uuid, operadorDto);
        var message = messageSource.getMessage("operador.update.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/operadores";
    }

    @DeleteMapping("/{uuid}")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirect) {
        operadorService.deleteById(uuid);
        var message = messageSource.getMessage("operador.delete.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/operadores";
    }

    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedCadastroBreadcrumb = messageSource.getMessage("cadastro.breadcrumb", null, currentLocale);
        String localizedOperadorBreadcrumb = messageSource.getMessage("operador.breadcrumb", null, currentLocale);
        return List.of(

                new BreadcrumbsController.BreadcrumbItem(localizedCadastroBreadcrumb, null),
                new BreadcrumbsController.BreadcrumbItem(localizedOperadorBreadcrumb, "/operador")
        );
    }
}

