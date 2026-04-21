package br.com.fiap.sustentabilidade.repository;

import br.com.fiap.sustentabilidade.model.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<Setor, Long> {
    Page<Setor> findByAtivo(Integer ativo, Pageable pageable);
}
