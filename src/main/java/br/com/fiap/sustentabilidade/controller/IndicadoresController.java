package br.com.fiap.sustentabilidade.controller;

import br.com.fiap.sustentabilidade.dto.IndicadoresResponse;
import br.com.fiap.sustentabilidade.service.IndicadoresService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicadores")
public class IndicadoresController {

    private final IndicadoresService service;

    public IndicadoresController(IndicadoresService service) {
        this.service = service;
    }

    // GET /api/indicadores/sustentabilidade?de=&ate=
    @GetMapping("/sustentabilidade")
    public IndicadoresResponse indicadores(
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String ate) {
        return service.calcular(de, ate);
    }
}
