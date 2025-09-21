package com.br.code_crafters.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class BreadcrumbsController {

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

    @GetMapping("/index")
    public String index(Model model) {
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

    @GetMapping("/filiais")
    public String filial(Model model){
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null),
                new BreadcrumbItem("Filial", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/filiais";
    }

    @GetMapping("/sensores")
    public String sensores(Model model){
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null),
                new BreadcrumbItem("Sensores", null)
        );

        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/sensores";
    }

    @GetMapping("/patios")
    public String patios(Model model){
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null),
                new BreadcrumbItem("Patios", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/patios";
    }

    @GetMapping("/motos")
    public String motos(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null),
                new BreadcrumbItem("Motos", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        model.addAttribute("pageTitleKey", "moto.cadastro.titulo");
        return "fragments/motos";
    }

    @GetMapping("/monitoramento")
    public String monitoramento(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null),
                new BreadcrumbItem("Monitoramento", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/monitoramento";
    }

    @GetMapping("/ajustes")
    public String ajustes(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null),
                new BreadcrumbItem("Ajustes", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/ajustes";
    }
}