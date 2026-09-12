package br.com.fiap.xaelor_mvc.repository;

import br.com.fiap.xaelor_mvc.model.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
}
