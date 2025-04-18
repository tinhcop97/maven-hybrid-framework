package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import commons.Constants;

public class AuthAPI {
    public Response login(String retailer, String username, String password) {
        return given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .header("retailer", retailer)
                .formParam("provider", "credentials")
                .formParam("UserName", username)
                .formParam("Password", password)
                .formParam("IsManageLogin", "true")
                .when()
                .post(Constants.API_LOGIN_URL)
                .then()
                .extract()
                .response();
    }
}
