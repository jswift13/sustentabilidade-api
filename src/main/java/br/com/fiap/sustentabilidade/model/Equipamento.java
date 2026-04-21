package br.com.fiap.sustentabilidade.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "EQUIPAMENTOS")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EQUIPAMENTO")
    private Long id;

    @Column(name = "ID_SETOR", nullable = false)
    private Long idSetor;

    @Column(name = "NOME_EQUIPAMENTO", nullable = false, length = 100)
    private String nomeEquipamento;

    @Column(name = "TIPO_EQUIPAMENTO", length = 50)
    private String tipoEquipamento;

    @Column(name = "POTENCIA_WATTS", nullable = false)
    private Double potenciaWatts;

    @Column(name = "STATUS", length = 20)
    private String status; // ATIVO | INATIVO

    @Column(name = "DATA_INSTALACAO")
    private LocalDate dataInstalacao;

    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt; // preenchido pelo DB (default)

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdSetor() { return idSetor; }
    public void setIdSetor(Long idSetor) { this.idSetor = idSetor; }
    public String getNomeEquipamento() { return nomeEquipamento; }
    public void setNomeEquipamento(String nomeEquipamento) { this.nomeEquipamento = nomeEquipamento; }
    public String getTipoEquipamento() { return tipoEquipamento; }
    public void setTipoEquipamento(String tipoEquipamento) { this.tipoEquipamento = tipoEquipamento; }
    public Double getPotenciaWatts() { return potenciaWatts; }
    public void setPotenciaWatts(Double potenciaWatts) { this.potenciaWatts = potenciaWatts; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataInstalacao() { return dataInstalacao; }
    public void setDataInstalacao(LocalDate dataInstalacao) { this.dataInstalacao = dataInstalacao; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
