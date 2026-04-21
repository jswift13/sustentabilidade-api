package br.com.fiap.sustentabilidade.dto;

public class IndicadoresResponse {
    public String de;                 // ISO (yyyy-MM-dd)
    public String ate;                // ISO
    public double consumo_total_kwh;  // soma CONSUMO_ENERGIA
    public double geracao_total_kwh;  // soma GERACAO_SOLAR
    public double saldo_kwh;          // geracao - consumo
    public Double taxa_compensacao;   // geracao / consumo (null se consumo=0)
    public double economia_co2_kg;    // soma GERACAO_SOLAR.economia_co2_kg
}
