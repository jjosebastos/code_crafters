package com.br.code_crafters.filial;

import com.br.code_crafters.dto.FilialSearchResult;
import com.br.code_crafters.navigation.BreadcrumbsController;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/filiais") // <-- 1. ADICIONE ESTA LINHA
public class FilialController {

    private final FilialService filialService;
    private final MessageSource messageSource;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final int DEFAULT_LIMIT = 10;

    public FilialController(FilialService filialService, MessageSource messageSource) {
        this.filialService = filialService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String gerenciarFiliais(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        List<BreadcrumbsController.BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbsController.BreadcrumbItem("Cadastros", null),
                new BreadcrumbsController.BreadcrumbItem("Filial", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);

        if (!model.containsAttribute("filialCreate")) {
            model.addAttribute("filialCreate", new Filial());
        }
        if (!model.containsAttribute("filialEdit")) {
            model.addAttribute("filialEdit", new Filial());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<Filial> filialPage = filialService.findPaginated(pageable);
        var countFiliais = filialService.getFiliaisOrdenadas();
        model.addAttribute("countFiliais", countFiliais);
        model.addAttribute("filialPage", filialPage);

        return "fragments/filiais";
    }
    @PostMapping
    public String create(@ModelAttribute("filialCreate") @Valid Filial dto, BindingResult br, RedirectAttributes ra) {
        System.out.println("Dados recebidos do formulário: " + dto);
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.filialCreate", br);
            ra.addFlashAttribute("filialCreate", dto);
            return "redirect:/filiais";
        }
        filialService.create(dto);
        ra.addFlashAttribute("success", "Filial criada");
        return "redirect:/filiais"; // <-- 2. CORRIJA O REDIRECT
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Filial> searchFiliais(@RequestParam("q") String q, Pageable pageable) {
        return filialService.searchByNameOrCnpj(q, pageable);
    }

    @PutMapping("/{id}")
    public String atualizarFilial(@PathVariable("id") UUID id, @ModelAttribute Filial filial) {
        filialService.update(id, filial);
        return "redirect:/filiais";
    }
}