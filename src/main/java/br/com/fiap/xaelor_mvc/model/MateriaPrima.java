package br.com.fiap.xaelor_mvc.model;

import br.com.fiap.xaelor_mvc.enums.TipoUnidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TDS_MVC_TB_MATERIAPRIMA")
public class MateriaPrima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "MATPRIMA_ID")
    private Long id;

    @NotBlank(message = "O nome da matéria-prima é obrigatório")
    @Column(nullable = false, name = "MATPRIMA_NOME")
    private String nome;

    @NotNull(message = "O tipo de unidade é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "MATPRIMA_TIPOUNIDADE")
    private TipoUnidade tipoUnidade;

    @Column(name = "MATPRIMA_DESCRICAO")
    private String descricao;
}
