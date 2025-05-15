package testcases.api;

import api.AuthAPI;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;
import io.restassured.response.Response;
import commons.BaseTest;
import commons.ConfigReader;

public class AuthAPITest extends BaseTest {

    @Test
    public void testLoginSuccess() {
        // Get values directly from ConfigReader to ensure we're using the latest values
        String retailer = ConfigReader.getProperty("retailer");
        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");
        
        System.out.println("Test using credentials - Retailer: " + retailer + ", Username: " + username + ", Password: " + password);
        
        AuthAPI authAPI = new AuthAPI();
        Response response = authAPI.login(retailer, username, password);

        // Debug logs
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());
        System.out.println("Headers: " + response.getHeaders());

        String token = response.jsonPath().getString("BearerToken");
        System.out.println("Extracted BearerToken: " + token);

        if (token == null) {
            token = response.jsonPath().getString("token");
            System.out.println("Extracted token: " + token);
        }

        assertNotNull(token, "Authentication token should not be null");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        assertEquals(response.jsonPath().getString("UserName"), username, "Username in response should match");
    }
}
