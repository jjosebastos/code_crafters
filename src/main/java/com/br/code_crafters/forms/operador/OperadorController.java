package com.br.code_crafters.forms.operador;


import com.br.code_crafters.exception.CpfAlreadyExistsException;
import com.br.code_crafters.navigation.BreadcrumbsController;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
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
        String pageTitleKey = "operador.form.title.create";

        if (uuid != null) {
            Operador operador = operadorService.findById(uuid)
                    .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado com o ID: " + uuid));

            OperadorDto dto = new OperadorDto();
            dto.setIdOperador(operador.getIdOperador());
            dto.setNome(operador.getNome());
            dto.setCpf(operador.getCpf());
            dto.setRg(operador.getRg());

            model.addAttribute("operadorDto", dto);
            pageTitleKey = "operador.form.title.edit";
        } else {
            model.addAttribute("operadorDto", new OperadorDto());
        }

        model.addAttribute("pageTitleKey", pageTitleKey);
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/operadores/formOperadores";
    }

    @PostMapping
    public String postOperadores(@Valid @ModelAttribute("operadorDto") OperadorDto dto,
                                 BindingResult br,
                                 RedirectAttributes redirect,
                                 Model model) { // <-- 1. ADICIONADO MODEL

        String pageTitleKey = "operador.form.title.create";

        if (br.hasErrors()) {
            model.addAttribute("pageTitleKey", pageTitleKey);
            model.addAttribute("breadcrumb", createBreadcrumb());
            return "fragments/operadores/formOperadores";
        }

        try {
            operadorService.save(dto);
            var message = messageSource.getMessage("operador.create.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message);
            return "redirect:/operadores";

        } catch (CpfAlreadyExistsException e) {
            String errorMessage = messageSource.getMessage("operador.cpf.unique", null, LocaleContextHolder.getLocale());
            br.addError(new FieldError("operadorDto", "cpf", dto.getCpf(), false, null, null, errorMessage));

        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Erro de banco de dados: " + e.getMostSpecificCause().getMessage();
            br.addError(new FieldError("operadorDto", "", null, false, null, null, errorMessage));
        }

        model.addAttribute("pageTitleKey", pageTitleKey);
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/operadores/formOperadores";
    }

    @PutMapping("/{uuid}")
    public String update(@PathVariable UUID uuid,
                         @Valid @ModelAttribute("operadorDto") OperadorDto operadorDto,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Model model) {

        String pageTitleKey = "operador.form.title.edit";

        if (result.hasErrors()) {
            model.addAttribute("pageTitleKey", pageTitleKey);
            model.addAttribute("breadcrumb", createBreadcrumb());
            return "fragments/operadores/formOperadores";
        }

        try {
            operadorService.update(uuid, operadorDto);
            var message = messageSource.getMessage("operador.update.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message);
            return "redirect:/operadores";

        } catch (CpfAlreadyExistsException e) {
            String errorMessage = messageSource.getMessage("operador.cpf.unique", null, LocaleContextHolder.getLocale());
            result.addError(new FieldError("operadorDto", "cpf", operadorDto.getCpf(), false, null, null, errorMessage));

        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Erro de banco de dados: " + e.getMostSpecificCause().getMessage();
            result.addError(new FieldError("operadorDto", "", null, false, null, null, errorMessage));
        }

        model.addAttribute("pageTitleKey", pageTitleKey);
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/operadores/formOperadores";
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