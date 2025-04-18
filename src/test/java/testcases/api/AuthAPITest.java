package testcases.api;

import api.AuthAPI;
import commons.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AuthAPITest extends BaseTest {
    @Test
    public void testLoginSuccess() {
        AuthAPI authAPI = new AuthAPI();
        Response response = authAPI.login("tinhcoinew", "valid_user", "valid_pass");
        
        assertEquals(response.getStatusCode(), 200);
        // Add more assertions as needed
    }
}
