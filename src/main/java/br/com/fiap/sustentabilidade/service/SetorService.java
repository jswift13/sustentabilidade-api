package br.com.fiap.sustentabilidade.service;

import br.com.fiap.sustentabilidade.dto.SetorUpdateDTO;
import br.com.fiap.sustentabilidade.exception.ApiException;
import br.com.fiap.sustentabilidade.model.Setor;
import br.com.fiap.sustentabilidade.repository.SetorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetorService {

    private final SetorRepository setores;

    public SetorService(SetorRepository setores) {
        this.setores = setores;
    }

    @Transactional
    public Setor atualizar(Long id, SetorUpdateDTO dto) {
        var s = setores.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Setor não encontrado"));

        if (dto.nome != null) {
            if (dto.nome.isBlank()) throw new ApiException(HttpStatus.BAD_REQUEST, "nome não pode ser vazio");
            s.setNome(dto.nome);
        }
        if (dto.areaM2 != null) {
            if (dto.areaM2 < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "area_m2 deve ser >= 0");
            s.setAreaM2(dto.areaM2);
        }
        if (dto.metaConsumoKwh != null) {
            if (dto.metaConsumoKwh < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "meta_consumo_kwh deve ser >= 0");
            s.setMetaConsumoKwh(dto.metaConsumoKwh);
        }
        if (dto.responsavel != null) {
            s.setResponsavel(dto.responsavel);
        }
        if (dto.ativo != null) {
            if (dto.ativo != 0 && dto.ativo != 1) throw new ApiException(HttpStatus.BAD_REQUEST, "ativo deve ser 0 ou 1");
            s.setAtivo(dto.ativo);
        }

        return setores.save(s);
    }
}
