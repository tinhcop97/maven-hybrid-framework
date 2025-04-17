package api;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import commons.BaseTest;
import commons.Constants;

import static io.restassured.RestAssured.given;

public class AuthAPITest extends BaseTest {

    private final String retailer = "tinhcoinew";

    @Test
    public void testLoginSuccess() {
        String testUsername = System.getenv().getOrDefault("TEST_USERNAME", "admin");
        String testPassword = System.getenv().getOrDefault("TEST_PASSWORD", "123");
        
        Response response = authAPI.login(retailer, testUsername, testPassword);

        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("token"));
        assertEquals(response.jsonPath().getString("preferred_username"), testUsername);
        // Additional assertions
        assertNotNull(response.jsonPath().getString("expires_in"));
        assertNotNull(response.jsonPath().getString("token_type"));
    }

    @Test
    public void testLoginInvalidCredentials() {
        Response response = authAPI.login(retailer, "wronguser", "wrongpass");

        assertEquals(response.getStatusCode(), 401);
        // Optional: kiểm tra nếu API trả về error chi tiết
        String errorMsg = response.jsonPath().getString("error");
        assertTrue(errorMsg != null && !errorMsg.isEmpty());
    }

    @Test
    public void testLoginMissingParameters() {
        Response response = given()
                .baseUri(Constants.BASE_URL)
                .header("retailer", retailer)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("provider", "credentials")
                .when()
                .post(Constants.API_LOGIN_URL);

        assertEquals(response.getStatusCode(), 400); // hoặc mã lỗi phù hợp
    }
}
