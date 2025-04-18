package api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;
import io.restassured.response.Response;
import commons.BaseTest;
import commons.Constants;

public class AuthAPITest extends BaseTest {

    private final String retailer = "tinhcoinew";

    @Test
    public void testLoginSuccess() {
        AuthAPI authAPI = new AuthAPI();
        Response response = authAPI.login(retailer, "admin", "123");

        // Debug logs
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());
        System.out.println("Headers: " + response.getHeaders());

        String token = response.jsonPath().getString("BearerToken");
        System.out.println("Extracted BearerToken: " + token);

        assertNotNull(token);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("UserName"), "admin");
    }

}