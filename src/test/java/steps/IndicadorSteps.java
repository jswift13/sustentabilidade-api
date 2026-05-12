package steps;

import io.cucumber.java.pt.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import services.IndicadorService;

public class IndicadorSteps {
    IndicadorService service = new IndicadorService();
    Response response;

    @Quando("eu consultar todos os setores")
    public void consultarSetores() {
        response = service.listarSetores();
    }

    @Quando("eu cadastrar um equipamento com nome {string} e potencia {int} para o setor {int}")
    public void cadastrarEquip(String nome, int pot, int setorId) {
    // Trocando de "id_setor" para "idSetor"
    String json = String.format("{\"nome\":\"%s\", \"potenciaWatts\":%d, \"status\":\"ATIVO\", \"idSetor\":%d}", nome, pot, setorId);
        response = service.criarEquipamento(json);
    }

    @Quando("eu consultar indicadores de {string} ate {string}")
    public void consultarIndicadores(String de, String ate) {
        response = service.consultarIndicadores(de, ate);
    }

    @Quando("eu deletar o equipamento com ID {int}")
    public void deletarEquip(int id) {
        response = service.deletarEquipamento((long) id);
    }

    @Então("o status code deve ser {int}")
    public void validarStatus(int code) {
        response.then().statusCode(code);
    }

    @Então("o contrato deve ser validado pelo arquivo {string}")
    public void validarSchema(String arquivo) {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + arquivo));
    }
}