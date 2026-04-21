package br.com.fiap.sustentabilidade.controller;

import br.com.fiap.sustentabilidade.dto.RankingSetorItem;
import br.com.fiap.sustentabilidade.service.RankingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService service;

    public RankingController(RankingService service) {
        this.service = service;
    }

    // GET /api/ranking/setores?de=&ate=
    @GetMapping("/setores")
    public List<RankingSetorItem> rankingSetores(
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String ate) {
        return service.listar(de, ate);
    }
}
