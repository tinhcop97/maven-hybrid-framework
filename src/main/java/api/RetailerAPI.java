package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import commons.Constants;

public class RetailerAPI {
    public Response getCurrentSession(String authToken,String retailer, String cookie) {
        return given()
                .header("Authorization", "Bearer " + authToken)
                .header("retailer", retailer)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", cookie) // truyền cookie từ biến ngoài
                .when()
                .get(Constants.API_CURRENT_SESSION_URL)
                .then()
                .extract()
                .response();
    }
}
