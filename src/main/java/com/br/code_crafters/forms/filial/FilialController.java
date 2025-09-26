package com.br.code_crafters.forms.filial;

import com.br.code_crafters.forms.operador.Operador;
import com.br.code_crafters.forms.operador.OperadorDto;
import com.br.code_crafters.navigation.BreadcrumbsController;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;
    private final MessageSource messageSource;

    public FilialController(FilialService filialService, MessageSource messageSource) {
        this.filialService = filialService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showAll(@RequestParam(required = false) String search,
                          @PageableDefault(size = 10, sort = "nmFilial") Pageable pageable,
                          Model model){
        Page<Filial> filiaisPage;
        if(search != null && !search.trim().isEmpty()){
            filiaisPage = filialService.findByNomeOrCnpj(search, search, pageable);
        } else {
            filiaisPage = filialService.findAll(pageable);
        }

        model.addAttribute("filiaisPage", filiaisPage);
        model.addAttribute("breadcrumb", createBreadcrumb());

        if(search != null){
            model.addAttribute("search", search);
        }

        return "fragments/filiais/listagemFiliais";

    }


    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) UUID uuid, Model model){
        if(uuid != null) {
            Filial filial = filialService.findById(uuid).orElseThrow(() -> new IllegalArgumentException("Operador não encontrado com o ID: " + uuid));
            model.addAttribute("filialDto", filial);
            model.addAttribute("pageTitleKey", "operador.form.title.edit");
        } else {
            model.addAttribute("filialDto", new FilialDto());
            model.addAttribute("pageTitleKey", "filial.form.title.create" );
        }
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/filiais/formFiliais";
    }


    @PutMapping("/{uuid}")
    public String update(@PathVariable UUID uuid,
                         @Valid @ModelAttribute("filialDto") FilialDto filialDto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            // If validation fails, return to the form page to show errors.
            return "fragments/filiais/formFiliais";
        }
        filialService.update(uuid, filialDto);
        var message = messageSource.getMessage("filial.update.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/filiais";
    }

    @DeleteMapping("/{uuid}")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirect) {
        filialService.deleteById(uuid);
        var message = messageSource.getMessage("filial.delete.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/filiais";
    }

    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        return List.of(
                new BreadcrumbsController.BreadcrumbItem("Cadastros", null),
                new BreadcrumbsController.BreadcrumbItem("Filiais", null)
        );
    }
}