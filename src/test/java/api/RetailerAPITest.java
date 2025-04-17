package api;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import io.restassured.response.Response;
import commons.BaseTest;
import commons.Constants;

import static io.restassured.RestAssured.given;

public class RetailerAPITest extends BaseTest {
    private String authToken;
    private final String retailer = "tinhcoinew";

    @BeforeMethod
    public void setup() {
        // Get valid token before each test
        authToken = authAPI.login(retailer, "admin", "123").jsonPath().getString("token");
    }

    @Test
    public void testGetCurrentSessionWithValidToken() {
        Response response = retailerAPI.getCurrentSession(authToken, retailer);

        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("kvugvname"), retailer);
        assertTrue(response.jsonPath().getBoolean("kvadmin"));
    }

    @Test
    public void testGetCurrentSessionWithInvalidToken() {
        Response response = retailerAPI.getCurrentSession("invalidtoken", retailer);

        assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testGetCurrentSessionWithoutToken() {
        Response response = given()
                .baseUri(Constants.BASE_URL) // đảm bảo BASE_URL có gán "https://phoenix.kvfnb.vip"
                .header("retailer", retailer)
                .when()
                .get("/api/retailers/currentsession?format=json");

        assertEquals(response.getStatusCode(), 401);
    }
}
