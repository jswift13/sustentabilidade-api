package model;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class IndicadorModel {
    private Double consumoTotal;
    private Double geracaoTotal;
    private Double saldo;
    private Double taxaCompensacao;
    private Double economiaCo2;
}