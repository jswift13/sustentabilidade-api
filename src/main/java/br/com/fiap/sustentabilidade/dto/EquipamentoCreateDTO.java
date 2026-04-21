package br.com.fiap.sustentabilidade.dto;

public class EquipamentoCreateDTO {
    public Long idSetor;
    public String nomeEquipamento;
    public String tipoEquipamento;
    public Double potenciaWatts;
    public String status;          // ATIVO|INATIVO (opcional)
    public String dataInstalacao;  // "2023-01-15" (ISO) - opcional
}
