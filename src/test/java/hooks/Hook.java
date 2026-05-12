package hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hook {
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        
        // Credenciais
        RestAssured.authentication = RestAssured.basic("admin", "admin123"); 

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}