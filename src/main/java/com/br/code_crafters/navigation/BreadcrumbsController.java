package com.br.code_crafters.navigation;

import com.br.code_crafters.forms.filial.Filial;
import com.br.code_crafters.forms.filial.FilialService;
import com.br.code_crafters.forms.patio.Patio;
import com.br.code_crafters.forms.patio.PatioService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class BreadcrumbsController {


    private final FilialService filialService;
    private final PatioService patioService;
    private final MessageSource messageSource;

    public BreadcrumbsController(FilialService filialService, PatioService patioService, MessageSource messageSource) {
        this.filialService = filialService;
        this.patioService = patioService;
        this.messageSource = messageSource;
    }

    public static class BreadcrumbItem {
        private String name;
        private String url;

        public BreadcrumbItem(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

    @GetMapping("/contato")
    public String showContact(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Contato", null)
        );
        model.addAttribute("breadcrumbPath", breadcrumbPath);
        model.addAttribute("pageTitleKey", "pageTitle.contact"); // Para internacionalização
        return "fragments/contato";
    }
    @GetMapping({"/", "/index", "/dashboard"})
    public String showHomePage(Model model) {
        model.addAttribute("totalUsers", 1234); // Exemplo
        model.addAttribute("usuariosLabels", Arrays.asList("Abr", "Mai", "Jun", "Jul", "Ago", "Set"));
        model.addAttribute("usuariosData", Arrays.asList(12, 25, 18, 30, 28, 35));

        model.addAttribute("totalFiliais", 56); // Exemplo
        model.addAttribute("filiaisLabels", Arrays.asList("SP", "RJ", "MG", "BA", "PR"));
        model.addAttribute("filiaisData", Arrays.asList(20, 12, 8, 6, 10));

        model.addAttribute("motosAtivasLabels", Arrays.asList("MottuPop", "MottuE", "MottuSport", "Outros"));
        model.addAttribute("motosAtivasData", Arrays.asList(450, 280, 120, 50)); // Exemplo de quantidades

        // NOVOS DADOS para o Card: Motos: No Pátio vs. Em Uso
        model.addAttribute("patiosLabels", Arrays.asList("No Pátio", "Fora do Pátio"));
        model.addAttribute("patiosData", Arrays.asList(300, 550)); // Exemplo de quantidades

        model.addAttribute("breadcrumb", List.of());
        return "index"; // Retorna o template 'index.html'
    }

    @GetMapping("/cadastros")
    public String cadastros(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/cadastros";
    }


    @GetMapping("/monitoramento")
    public String monitoramento(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Home", null),
                new BreadcrumbItem("Monitoramento", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/monitoramento";
    }

    @GetMapping("/ajustes")
    public String ajustes(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Home", null),
                new BreadcrumbItem("Ajustes", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/ajustes";
    }


}