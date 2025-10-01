    package com.br.code_crafters.forms.monitoring;

    import com.br.code_crafters.forms.patio.Patio;
    import com.br.code_crafters.forms.patio.PatioService;
    import com.br.code_crafters.navigation.BreadcrumbsController;
    import org.springframework.context.MessageSource;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.web.PageableDefault;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    import java.util.List;
    import java.util.UUID;

    @Controller
    @RequestMapping("/monitoramento")
    public class MonitoramentoController {

        private final PatioService patioService;
        private final MonitoramentoService monitoramentoService;
        private final MessageSource messageSource;

        public MonitoramentoController(PatioService patioService, MonitoramentoService monitoramentoService, MessageSource messageSource) {
            this.patioService = patioService;
            this.monitoramentoService = monitoramentoService;
            this.messageSource = messageSource;
        }

        @GetMapping
        public String monitoramento(Model model,
                                    @PageableDefault(size = 50, sort = "nmPatio") Pageable pageable,
                                    @RequestParam(required = false) UUID patioId,
                                    @RequestParam(required = false, defaultValue = "8") int cols,
                                    @RequestParam(required = false, defaultValue = "0") int totalSlots) {

            model.addAttribute("breadcrumb", List.of(new BreadcrumbsController.BreadcrumbItem("Monitoramento", null)));

            // 1) Lista paginada (se usar em tabelas/consulta)
            model.addAttribute("patiosPage", patioService.findAll(pageable));

            // 2) Lista completa para o SELECT (garante que o selecionado esteja presente)
            List<Patio> allPatios = patioService.findAllList(); // implementar no service (ou usar repo.findAll(Sort.by("nmPatio")))
            model.addAttribute("patios", allPatios);

            model.addAttribute("selectedPatioId", patioId);
            model.addAttribute("cols", cols);

            if (patioId != null) {
                var grid = monitoramentoService.buildGrid(patioId, cols, totalSlots);
                int occupied = monitoramentoService.countOccupied(patioId);
                int total = grid.stream().mapToInt(List::size).sum();

                model.addAttribute("grid", grid);
                model.addAttribute("totalSlots", total);
                model.addAttribute("occupied", occupied);
                model.addAttribute("totalSlotsParam", totalSlots); // se quiser devolver o valor parametrizado
            }

            return "fragments/monitoramento";
        }
    }
