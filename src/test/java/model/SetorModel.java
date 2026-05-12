package model;
import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class SetorModel {
    private Long id;
    private String nome;
    private Double areaM2;
    private Double metaConsumoKwh;
    private String responsavel;
    private Integer ativo;
}