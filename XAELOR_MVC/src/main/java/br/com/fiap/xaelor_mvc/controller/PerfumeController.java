package br.com.fiap.xaelor_mvc.controller;

import br.com.fiap.xaelor_mvc.model.Perfume;
import br.com.fiap.xaelor_mvc.service.PerfumeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfumes")
public class PerfumeController {

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    // Rota pública - qualquer visitante pode ver a lista e os detalhes (READ)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("perfumes", perfumeService.listar());
        return "perfumes/list";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        model.addAttribute("perfume", perfumeService.buscarPorId(id));
        return "perfumes/detail";
    }

    // Rotas privadas - exigem login (CREATE, UPDATE, DELETE)
    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("perfume", new Perfume());
        model.addAttribute("modoEdicao", false);
        return "perfumes/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("perfume") Perfume perfume,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "perfumes/form";
        }
        perfumeService.salvar(perfume);
        redirectAttributes.addFlashAttribute("mensagem", "Perfume cadastrado com sucesso!");
        return "redirect:/perfumes";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("perfume", perfumeService.buscarPorId(id));
        model.addAttribute("modoEdicao", true);
        return "perfumes/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("perfume") Perfume perfume,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("modoEdicao", true);
            return "perfumes/form";
        }
        perfumeService.atualizar(id, perfume);
        redirectAttributes.addFlashAttribute("mensagem", "Perfume atualizado com sucesso!");
        return "redirect:/perfumes";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        perfumeService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("mensagem", "Perfume removido com sucesso!");
        return "redirect:/perfumes";
    }
}
