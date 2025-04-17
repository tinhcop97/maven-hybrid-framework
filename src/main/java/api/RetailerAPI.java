package api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import commons.Constants;

public class RetailerAPI {

    public Response getCurrentSession(String token, String retailer) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("retailer", retailer)
                .header("Authorization", "Bearer " + token)
                .get("/api/retailers/currentsession?format=json");
    }
}
