package com.br.code_crafters.forms.moto;

import com.br.code_crafters.navigation.BreadcrumbsController;
import com.br.code_crafters.forms.patio.PatioService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class MotoController {

    private final PatioService patioService;
    private final MotoService motoService;
    private final MessageSource messageSource;


    public MotoController(PatioService patioService, MotoService motoService, MessageSource messageSource) {
        this.patioService = patioService;
        this.motoService = motoService;
        this.messageSource = messageSource;
    }




    @GetMapping("/motos")
    public String motos(Model model) {
        List<BreadcrumbsController.BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbsController.BreadcrumbItem("Cadastros", null),
                new BreadcrumbsController.BreadcrumbItem("Motos", null)
        );
        model.addAttribute("motoDto", new MotoDto());
        model.addAttribute("patios", patioService.findAll());
        model.addAttribute("motos", motoService.findAll());
        model.addAttribute("breadcrumb", breadcrumbPath);
        model.addAttribute("pageTitleKey", "moto.cadastro.titulo");
        return "fragments/motos";
    }

    @PostMapping("/motos")
    public String save(@Valid MotoDto motoDto, BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()) return "form";
        motoService.saveMoto(motoDto);
        var message = messageSource.getMessage("moto.create.success", null, LocaleContextHolder.getLocale());
        redirect.addAttribute("motos", motoService.findAll());
        redirect.addAttribute("patios", motoService.findAll());
        redirect.addFlashAttribute("message", message);
        return "redirect:/index";
    }

    @DeleteMapping("/motos/{id}")
    public String deletarMoto(@PathVariable UUID id) {
        motoService.deleteById(id);
        return "redirect:/motos";
    }


}
