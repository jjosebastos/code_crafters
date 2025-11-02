package com.br.code_crafters.navigation;


import com.br.code_crafters.forms.monitoring.ChartData;
import com.br.code_crafters.forms.monitoring.DashboardService;
import com.br.code_crafters.forms.patio.PatioService;
import com.br.code_crafters.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class BreadcrumbsController {

    private final MessageSource messageSource;
    private final PatioService patioService;
    private final UserService userService;
    private final DashboardService dashboardService;

    @Getter
    public static class BreadcrumbItem {
        private final String name;
        private final String url;
        public BreadcrumbItem(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }


    public String getLocalizedMessage(String key) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, currentLocale);
    }

    @GetMapping({"/", "/index", "/dashboard"})
    public String showHomePage(Model model) {
        var registrationData = dashboardService.getRegistrationChartData();
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("usuariosLabels", registrationData.labels());
        model.addAttribute("usuariosData", registrationData.data());
        var filiaisChartData = dashboardService.getFiliaisChartData();
        var totalFiliais = dashboardService.countFiliais();

        model.addAttribute("totalFiliais", totalFiliais);
        model.addAttribute("filiaisLabels", filiaisChartData.labels());
        model.addAttribute("filiaisData", filiaisChartData.data());

        var modelData = dashboardService.getActivesByModel();
        model.addAttribute("motosAtivasLabels", modelData.labels());
        model.addAttribute("motosAtivasData", modelData.data());

        var patiosData = dashboardService.getPatiosChartData();
        model.addAttribute("patiosLabels", patiosData.labels());
        model.addAttribute("patiosData", patiosData.data());
        model.addAttribute("motosNoPatio", patiosData.motosNoPatio());
        model.addAttribute("motosForaDoPatio", patiosData.motosForaDoPatio());
        return "index";
    }

    @GetMapping("/cadastros")
    public String cadastros(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Cadastros", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/cadastros";
    }



    @GetMapping("/ajustes")
    public String ajustes(Model model) {
        List<BreadcrumbItem> breadcrumbPath = List.of(
                new BreadcrumbItem("Ajustes", null)
        );
        model.addAttribute("breadcrumb", breadcrumbPath);
        return "fragments/ajustes";
    }


}