package services;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class IndicadorService {

    public Response listarSetores() {
        return given().get("/api/setores");
    }

    public Response criarEquipamento(String json) {
        return given().contentType(ContentType.JSON).body(json).post("/api/equipamentos");
    }

    public Response consultarIndicadores(String de, String ate) {
        return given()
                .queryParam("de", de)
                .queryParam("ate", ate)
                .get("/api/indicadores/sustentabilidade");
    }

    public Response deletarEquipamento(Long id) {
        return given().delete("/api/equipamentos/" + id);
    }
}