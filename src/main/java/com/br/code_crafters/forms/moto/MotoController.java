package com.br.code_crafters.forms.moto;

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
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;
    private final MessageSource messageSource;

    public MotoController(MotoService motoService, MessageSource messageSource) {
        this.motoService = motoService;
        this.messageSource = messageSource;
    }


    @GetMapping
    public String showAll(@RequestParam(required = false) String search,
                          @PageableDefault(size = 10, sort = "nrPlaca") Pageable pageable,
                          Model model
                          ){
        Page<Moto> motosPage = null;
        MotoSensorDto motoSensorDto = null;

        if(search != null && !search.trim().isEmpty()){
            motoSensorDto = motoService.findMotoSensorByPlaca(search);
        } else {
            motosPage = motoService.findAll(pageable);
        }

        model.addAttribute("motosPage", motosPage);
        model.addAttribute("motoSensorDto", motoSensorDto);
        model.addAttribute("breadcrumb", createBreadcrumb());

        if(search != null){
            model.addAttribute("search", search);
        }

        return "fragments/motos/motosListagem";

    }



    @PutMapping("/{uuid}")
    public String updateMoto(@PathVariable UUID uuid,
                             @Valid @ModelAttribute("motoDto") MotoDto motoDto,
                             BindingResult br,
                             RedirectAttributes redirect){
        if(br.hasErrors()){
            return "fragments/motos/motosForm";
        }
        motoService.update(uuid, motoDto);
        var message = messageSource.getMessage("moto.update.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/motos";
    }

    @DeleteMapping("/{id}")
    public String deletarMoto(@PathVariable UUID id,
                              RedirectAttributes redirect) {
        motoService.deleteById(id);
        var message = messageSource.getMessage("moto.delete.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/motos";
    }

    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        return List.of(
                new BreadcrumbsController.BreadcrumbItem("Cadastros", null),
                new BreadcrumbsController.BreadcrumbItem("Motos", null)
        );
    }

}
