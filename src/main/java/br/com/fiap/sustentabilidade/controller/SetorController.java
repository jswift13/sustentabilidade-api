package br.com.fiap.sustentabilidade.controller;

import br.com.fiap.sustentabilidade.dto.SetorUpdateDTO;
import br.com.fiap.sustentabilidade.model.Setor;
import br.com.fiap.sustentabilidade.repository.SetorRepository;
import br.com.fiap.sustentabilidade.service.SetorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setores")
public class SetorController {

    private final SetorRepository repo;
    private final SetorService service;

    public SetorController(SetorRepository repo, SetorService service) {
        this.repo = repo;
        this.service = service;
    }

    // GET /api/setores?ativo=&page=&size=
    @GetMapping
    public Page<Setor> listar(
            @RequestParam(required = false) Integer ativo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var pageable = PageRequest.of(page, size);
        if (ativo == null) return repo.findAll(pageable);
        return repo.findByAtivo(ativo, pageable);
    }

    // PUT /api/setores/{id}
    @PutMapping("/{id}")
    public Setor atualizar(@PathVariable Long id, @RequestBody SetorUpdateDTO dto) {
        return service.atualizar(id, dto);
    }
}
