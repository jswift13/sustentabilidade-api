package br.com.fiap.sustentabilidade.dto;

public class RankingSetorItem {
    public Long   setor_id;
    public String nome_setor;
    public double consumo_total_kwh;
    public double meta_consumo_kwh;
    public double eficiencia_percentual; // ((meta - consumo)/meta)*100 , se meta>0 senão 0
    public String classificacao;         // EXCELENTE/BOM/REGULAR/CRITICO
    public int    ranking;               // 1 = melhor
}
