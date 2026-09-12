package br.com.fiap.xaelor_mvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TDS_MVC_TB_PERFUME")
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERFUME_ID")
    private Long id;

    @NotBlank(message = "O nome do perfume é obrigatório")
    @Column(nullable = false, name = "PERFUME_NOME")
    private String nomePerfume;

    @NotBlank(message = "O gênero do perfume é obrigatório")
    @Column(nullable = false, name = "PERFUME_GENERO")
    private String generoPerfume;

    @Column(name = "PERFUME_DESCRICAO")
    private String descricaoPerfume;

    @Column(name = "PERFUME_PRECO")
    private Double preco;
}
