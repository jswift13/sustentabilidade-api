package br.com.fiap.sustentabilidade.repository;

import br.com.fiap.sustentabilidade.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    boolean existsByNomeEquipamentoAndIdSetor(String nomeEquipamento, Long idSetor);
}
