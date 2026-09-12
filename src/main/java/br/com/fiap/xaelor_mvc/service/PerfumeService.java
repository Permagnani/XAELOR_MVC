package br.com.fiap.xaelor_mvc.service;

import br.com.fiap.xaelor_mvc.model.Perfume;
import br.com.fiap.xaelor_mvc.repository.PerfumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<Perfume> listar() {
        return perfumeRepository.findAll();
    }

    public Perfume buscarPorId(Long id) {
        return perfumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfume não encontrado, id: " + id));
    }

    public Perfume salvar(Perfume perfume) {
        return perfumeRepository.save(perfume);
    }

    public Perfume atualizar(Long id, Perfume perfume) {
        Perfume existente = buscarPorId(id);
        existente.setNomePerfume(perfume.getNomePerfume());
        existente.setGeneroPerfume(perfume.getGeneroPerfume());
        existente.setDescricaoPerfume(perfume.getDescricaoPerfume());
        existente.setPreco(perfume.getPreco());
        return perfumeRepository.save(existente);
    }

    public void deletarPorId(Long id) {
        perfumeRepository.deleteById(id);
    }
}
