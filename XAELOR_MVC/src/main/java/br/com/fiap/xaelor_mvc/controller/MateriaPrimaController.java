package br.com.fiap.xaelor_mvc.controller;

import br.com.fiap.xaelor_mvc.enums.TipoUnidade;
import br.com.fiap.xaelor_mvc.model.MateriaPrima;
import br.com.fiap.xaelor_mvc.service.MateriaPrimaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/materias-primas")
public class MateriaPrimaController {

    private final MateriaPrimaService materiaPrimaService;

    public MateriaPrimaController(MateriaPrimaService materiaPrimaService) {
        this.materiaPrimaService = materiaPrimaService;
    }

    // Rota pública - READ
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materiasPrimas", materiaPrimaService.listar());
        return "materiaprima/list";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        model.addAttribute("materiaPrima", materiaPrimaService.buscarPorId(id));
        return "materiaprima/detail";
    }

    // Rotas privadas - CREATE, UPDATE, DELETE
    @GetMapping("/nova")
    public String novoForm(Model model) {
        model.addAttribute("materiaPrima", new MateriaPrima());
        model.addAttribute("tiposUnidade", TipoUnidade.values());
        model.addAttribute("modoEdicao", false);
        return "materiaprima/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("materiaPrima") MateriaPrima materiaPrima,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tiposUnidade", TipoUnidade.values());
            model.addAttribute("modoEdicao", false);
            return "materiaprima/form";
        }
        materiaPrimaService.salvar(materiaPrima);
        redirectAttributes.addFlashAttribute("mensagem", "Matéria-prima cadastrada com sucesso!");
        return "redirect:/materias-primas";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("materiaPrima", materiaPrimaService.buscarPorId(id));
        model.addAttribute("tiposUnidade", TipoUnidade.values());
        model.addAttribute("modoEdicao", true);
        return "materiaprima/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("materiaPrima") MateriaPrima materiaPrima,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tiposUnidade", TipoUnidade.values());
            model.addAttribute("modoEdicao", true);
            return "materiaprima/form";
        }
        materiaPrimaService.atualizar(id, materiaPrima);
        redirectAttributes.addFlashAttribute("mensagem", "Matéria-prima atualizada com sucesso!");
        return "redirect:/materias-primas";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        materiaPrimaService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("mensagem", "Matéria-prima removida com sucesso!");
        return "redirect:/materias-primas";
    }
}
