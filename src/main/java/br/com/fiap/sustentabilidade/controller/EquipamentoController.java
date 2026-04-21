package br.com.fiap.sustentabilidade.controller;

import br.com.fiap.sustentabilidade.dto.EquipamentoCreateDTO;
import br.com.fiap.sustentabilidade.dto.EquipamentoResponseDTO;
import br.com.fiap.sustentabilidade.service.EquipamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private final EquipamentoService service;

    public EquipamentoController(EquipamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> criar(@RequestBody EquipamentoCreateDTO dto) {
        var resp = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/api/equipamentos/" + resp.id))
                .body(resp);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build(); // 204
    }


}
