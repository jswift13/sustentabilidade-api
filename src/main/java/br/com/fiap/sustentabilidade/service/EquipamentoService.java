package br.com.fiap.sustentabilidade.service;

import br.com.fiap.sustentabilidade.dto.EquipamentoCreateDTO;
import br.com.fiap.sustentabilidade.dto.EquipamentoResponseDTO;
import br.com.fiap.sustentabilidade.exception.ApiException;
import br.com.fiap.sustentabilidade.model.Equipamento;
import br.com.fiap.sustentabilidade.model.Setor;
import br.com.fiap.sustentabilidade.repository.EquipamentoRepository;
import br.com.fiap.sustentabilidade.repository.SetorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentos;
    private final SetorRepository setores;

    public EquipamentoService(EquipamentoRepository equipamentos, SetorRepository setores) {
        this.equipamentos = equipamentos;
        this.setores = setores;
    }

    @Transactional
    public void inativar(Long id) {
        var e = equipamentos.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));

        if (!"INATIVO".equalsIgnoreCase(e.getStatus())) {
            e.setStatus("INATIVO");
            equipamentos.save(e);
        }
        // idempotente: se já estava INATIVO, não faz nada e não dá erro
    }

    public EquipamentoResponseDTO criar(EquipamentoCreateDTO dto) {
        // validações
        if (dto.idSetor == null) throw new ApiException(HttpStatus.BAD_REQUEST, "id_setor é obrigatório");
        if (dto.nomeEquipamento == null || dto.nomeEquipamento.isBlank())
            throw new ApiException(HttpStatus.BAD_REQUEST, "nome_equipamento é obrigatório");
        if (dto.potenciaWatts == null || dto.potenciaWatts <= 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "potencia_watts deve ser > 0");

        Setor setor = setores.findById(dto.idSetor)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Setor não encontrado"));

        if (setor.getAtivo() != null && setor.getAtivo() == 0)
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Setor inativo");

        if (dto.status == null || dto.status.isBlank()) dto.status = "ATIVO";
        if (!dto.status.equals("ATIVO") && !dto.status.equals("INATIVO"))
            throw new ApiException(HttpStatus.BAD_REQUEST, "status deve ser ATIVO ou INATIVO");

        // (opcional) regra de duplicidade por setor
        if (equipamentos.existsByNomeEquipamentoAndIdSetor(dto.nomeEquipamento, dto.idSetor))
            throw new ApiException(HttpStatus.CONFLICT, "Já existe equipamento com esse nome no setor");

        // mapear DTO -> entity
        var e = new Equipamento();
        e.setIdSetor(dto.idSetor);
        e.setNomeEquipamento(dto.nomeEquipamento);
        e.setTipoEquipamento(dto.tipoEquipamento);
        e.setPotenciaWatts(dto.potenciaWatts);
        e.setStatus(dto.status);
        if (dto.dataInstalacao != null && !dto.dataInstalacao.isBlank())
            e.setDataInstalacao(LocalDate.parse(dto.dataInstalacao)); // "YYYY-MM-DD"

        e = equipamentos.save(e);

        // entity -> response
        var resp = new EquipamentoResponseDTO();
        resp.id = e.getId();
        resp.idSetor = e.getIdSetor();
        resp.nomeEquipamento = e.getNomeEquipamento();
        resp.status = e.getStatus();
        return resp;
    }
}

