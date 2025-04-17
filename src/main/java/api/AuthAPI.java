package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import commons.Constants;

public class AuthAPI {
    public Response login(String username, String password) {
        return given()
            .contentType("application/json")
            .body(String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password))
            .when()
            .post(Constants.API_LOGIN_URL);
    }
}
