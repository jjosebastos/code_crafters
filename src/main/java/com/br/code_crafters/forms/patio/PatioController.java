package com.br.code_crafters.forms.patio;

import com.br.code_crafters.forms.filial.Filial;
import com.br.code_crafters.forms.operador.Operador;
import com.br.code_crafters.forms.operador.OperadorDto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/patios")
public class PatioController {

    private final PatioService patioService;
    private final MessageSource messageSource;

    public PatioController(PatioService patioService, MessageSource messageSource) {
        this.patioService = patioService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showAll(@RequestParam(required = false) String search,
                          @PageableDefault(size = 10, sort = "nmPatio") Pageable pageable,
                          Model model) {

        Page<Patio> patiosPage;
        if (search != null && !search.trim().isEmpty()) {
            patiosPage = patioService.findAllByNomeDescricao(search, search, pageable);
        } else {
            patiosPage = patioService.findAll(pageable);
        }

        model.addAttribute("filiais", patioService.finAllFiliais());
        model.addAttribute("patiosPage", patiosPage);
        model.addAttribute("breadcrumb", createBreadcrumb());

        if (search != null) {
            model.addAttribute("search", search);
        }
        return "fragments/patios/patioListagem";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) UUID uuid, Model model) {
        PatioDto patioDto;

        // Carrega a lista de filiais para o <select>, independentemente se é edição ou criação
        model.addAttribute("filiais", patioService.finAllFiliais());

        if (uuid != null) {
            var patio = patioService.findById(uuid)
                    .orElseThrow(() -> new IllegalArgumentException("Pátio não encontrado com o ID: " + uuid));
            patioDto = new PatioDto();
            patioDto.setIdPatio(patio.getIdPatio());
            patioDto.setNmPatio(patio.getNmPatio());
            patioDto.setDsPatio(patio.getDsPatio());
            if (patio.getFilial() != null) {
                patioDto.setIdFilial(patio.getFilial().getIdFilial());
            }
            model.addAttribute("pageTitleKey", "patio.form.title.edit");
        } else {
            patioDto = new PatioDto();
            model.addAttribute("pageTitleKey", "patio.form.title.create");
        }
        model.addAttribute("patioDto", patioDto);
        model.addAttribute("breadcrumb",  createBreadcrumb());
        return "fragments/patios/patioForm";
    }

    @PutMapping("/{uuid}")
    public String update(@PathVariable UUID uuid,
                         @Valid @ModelAttribute("operadorDto") PatioDto patioDto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "fragments/patios/formPatios";
        }
        patioService.updatePatio(uuid, patioDto);
        var message = messageSource.getMessage("patio.update.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/patios";
    }

    @DeleteMapping("/{uuid}")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirect){
        patioService.deletePatio(uuid);
        var message = messageSource.getMessage("patio.delete.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/patios";
    }

    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        return List.of(
                new BreadcrumbsController.BreadcrumbItem("Cadastros", null),
                new BreadcrumbsController.BreadcrumbItem("Patios", null)
        );
    }


}
