package com.br.code_crafters.forms.filial;

import com.br.code_crafters.forms.endereco.Endereco;
import com.br.code_crafters.forms.endereco.EnderecoRepository;
import com.br.code_crafters.navigation.BreadcrumbsController;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;
    private final MessageSource messageSource;
    private final EnderecoRepository enderecoRepository;

    public FilialController(FilialService filialService, MessageSource messageSource, EnderecoRepository enderecoRepository) {
        this.filialService = filialService;
        this.messageSource = messageSource;
        this.enderecoRepository = enderecoRepository;
    }

    // ... (Métodos showAll, showForm, update, create omitidos por brevidade) ...

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
            Filial filialEntity = filialService.findById(uuid)
                    .orElseThrow(() -> new IllegalArgumentException("Filial não encontrada: " + uuid));
            Endereco enderecoEntity = enderecoRepository.findByFilialIdFilial(uuid).orElse(new Endereco());
            FilialDto dto = getFilialDto(filialEntity, enderecoEntity);
            model.addAttribute("filialDto", dto);
            model.addAttribute("pageTitleKey", "filial.form.title.edit");
        } else {
            model.addAttribute("filialDto", new FilialDto());
            model.addAttribute("pageTitleKey", "filial.form.title.create");
        }
        model.addAttribute("breadcrumb", createBreadcrumb());
        return "fragments/filiais/formFiliais";
    }

    @PutMapping("/{uuid}")
    public String update(@PathVariable UUID uuid,
                         @Valid @ModelAttribute("filialDto") FilialDto filialDto,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("breadcrumb", createBreadcrumb());
            model.addAttribute("pageTitleKey", "filial.form.title.edit");
            return "fragments/filiais/formFiliais";
        }
        filialService.update(uuid, filialDto);
        var message = messageSource.getMessage("filial.update.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/filiais";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("filialDto") FilialDto dto, BindingResult br,
                         RedirectAttributes redirect,
                         Model model){
        if(br.hasErrors()) {
            model.addAttribute("breadcrumb", createBreadcrumb());
            model.addAttribute("pageTitleKey", "filial.form.title.create");
            return "fragments/filiais/formFiliais";
        }
        filialService.save(dto);
        var message = messageSource.getMessage("filial.create.success", null, LocaleContextHolder.getLocale());
        redirect.addFlashAttribute("message", message);
        return "redirect:/filiais";
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirect) {
        try {
            filialService.deleteById(uuid);
            var message = messageSource.getMessage("filial.delete.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message);
        } catch (DataIntegrityViolationException e) {
            var errorMessage = messageSource.getMessage("filial.delete.error.integrity", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("error", errorMessage); // Envia um atributo "error"
        }
        return "redirect:/filiais";
    }


    private List<BreadcrumbsController.BreadcrumbItem> createBreadcrumb() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedCadastroBreadcrumb = messageSource.getMessage("cadastro.breadcrumb", null, currentLocale);
        String localizedFilialBreadcrumb = messageSource.getMessage("filial.breadcrumb", null, currentLocale);
        return List.of(
                new BreadcrumbsController.BreadcrumbItem(localizedCadastroBreadcrumb, null),
                new BreadcrumbsController.BreadcrumbItem(localizedFilialBreadcrumb, "/filial")
        );
    }

    private static FilialDto getFilialDto(Filial filialEntity, Endereco enderecoEntity) {
        FilialDto dto = new FilialDto();
        dto.setIdFilial(filialEntity.getIdFilial());
        dto.setNmFilial(filialEntity.getNmFilial());
        dto.setNrCnpj(filialEntity.getNrCnpj());
        dto.setCdPais(filialEntity.getCdPais());
        dto.setNrCep(enderecoEntity.getNrCep());
        dto.setNmLogradouro(enderecoEntity.getNmLogradouro());
        dto.setNrLogradouro(enderecoEntity.getNrLogradouro());
        dto.setNmBairro(enderecoEntity.getNmBairro());
        dto.setNmCidade(enderecoEntity.getNmCidade());
        dto.setNmUf(enderecoEntity.getNmUf());
        dto.setDsComplemento(enderecoEntity.getDsComplemento());
        return dto;
    }
}