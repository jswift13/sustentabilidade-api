package model;
import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EquipamentoModel {
    private Long id;
    private String nome;
    private Integer potenciaWatts;
    private String status;
    private Long setorId;
}