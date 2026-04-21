package br.com.fiap.sustentabilidade.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SETORES") // tabela ESG.SETORES
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SETOR")
    private Long id;

    @Column(name = "NOME_SETOR", nullable = false, length = 100)
    private String nome;

    @Column(name = "AREA_M2")
    private Double areaM2;

    @Column(name = "META_CONSUMO_KWH")
    private Double metaConsumoKwh;

    @Column(name = "RESPONSAVEL")
    private String responsavel;

    @Column(name = "ATIVO")
    private Integer ativo;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getAreaM2() { return areaM2; }
    public void setAreaM2(Double areaM2) { this.areaM2 = areaM2; }
    public Double getMetaConsumoKwh() { return metaConsumoKwh; }
    public void setMetaConsumoKwh(Double metaConsumoKwh) { this.metaConsumoKwh = metaConsumoKwh; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public Integer getAtivo() { return ativo; }
    public void setAtivo(Integer ativo) { this.ativo = ativo; }
}
