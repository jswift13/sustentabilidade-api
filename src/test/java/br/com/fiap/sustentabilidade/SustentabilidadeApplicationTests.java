package br.com.fiap.sustentabilidade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SustentabilidadeApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void getSetores_comCredenciaisValidas_retorna200() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("user", "user123")
            .getForEntity("/api/setores", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getSetores_semCredenciais_retorna401() {
        ResponseEntity<String> response = restTemplate
            .getForEntity("/api/setores", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void postEquipamento_comUserComum_retorna403() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("user", "user123")
            .postForEntity("/api/equipamentos",
                "{\"idSetor\":1,\"nomeEquipamento\":\"Teste\",\"potenciaWatts\":100,\"status\":\"ATIVO\"}",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getIndicadores_comCredenciaisValidas_retorna200() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("user", "user123")
            .getForEntity(
                "/api/indicadores/sustentabilidade?de=2025-01-01&ate=2025-12-31",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getRanking_comCredenciaisValidas_retorna200() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("user", "user123")
            .getForEntity(
                "/api/ranking/setores?de=2025-01-01&ate=2025-12-31",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}