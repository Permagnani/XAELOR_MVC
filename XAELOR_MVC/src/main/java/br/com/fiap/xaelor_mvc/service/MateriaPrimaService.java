package br.com.fiap.xaelor_mvc.service;

import br.com.fiap.xaelor_mvc.model.MateriaPrima;
import br.com.fiap.xaelor_mvc.repository.MateriaPrimaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaPrimaService {

    private final MateriaPrimaRepository materiaPrimaRepository;

    public MateriaPrimaService(MateriaPrimaRepository materiaPrimaRepository) {
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    public List<MateriaPrima> listar() {
        return materiaPrimaRepository.findAll();
    }

    public MateriaPrima buscarPorId(Long id) {
        return materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria-prima não encontrada, id: " + id));
    }

    public MateriaPrima salvar(MateriaPrima materiaPrima) {
        return materiaPrimaRepository.save(materiaPrima);
    }

    public MateriaPrima atualizar(Long id, MateriaPrima materiaPrima) {
        MateriaPrima existente = buscarPorId(id);
        existente.setNome(materiaPrima.getNome());
        existente.setTipoUnidade(materiaPrima.getTipoUnidade());
        existente.setDescricao(materiaPrima.getDescricao());
        return materiaPrimaRepository.save(existente);
    }

    public void deletarPorId(Long id) {
        materiaPrimaRepository.deleteById(id);
    }
}
