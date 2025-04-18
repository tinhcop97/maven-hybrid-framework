package api;

import commons.Constants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;
import io.restassured.response.Response;
import commons.BaseTest;

public class RetailerAPITest extends BaseTest {
    private String authToken;
    private String sessionCookie; // Thêm biến lưu cookie
    private AuthAPI authAPI;
    private RetailerAPI retailerAPI; // Thêm dòng này ở trên

    @BeforeMethod
    public void setup() {
        authAPI = new AuthAPI();
        retailerAPI = new RetailerAPI();

        // Login và debug response
        Response loginResponse = authAPI.login("tinhcoinew", "admin", "123");
        System.out.println("Login Response: " + loginResponse.asString()); // Debug

        // Lấy token - kiểm tra cả key "token" và "BearerToken"
        authToken = loginResponse.jsonPath().getString("token");
        if (authToken == null) {
            authToken = loginResponse.jsonPath().getString("BearerToken");
        }
        System.out.println("Extracted Token: " + authToken); // Debug

        // Lấy cookie
        String cookieHeader = loginResponse.getHeader("Set-Cookie");
        if (cookieHeader != null) {
            sessionCookie = cookieHeader.split(";")[0];
        }
        System.out.println("Extracted Cookie: " + sessionCookie); // Debug

        // Verify không null
        assertNotNull(authToken, "Auth token không được null");
        assertNotNull(sessionCookie, "Session cookie không được null");
    }


    @Test
    public void testGetCurrentSessionWithValidToken() {
        Response response = retailerAPI.getCurrentSession(authToken, "tinhcoinew", sessionCookie);

        // Debug log để xem request headers
        System.out.println("=== Request Headers ===");
        System.out.println("Retailer: tinhcoinew");
        System.out.println("Authorization: Bearer " + authToken);
        System.out.println("Cookie: " + sessionCookie);

        // Debug log response
        System.out.println("=== Response ===");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Body: " + response.asString());

        // Assertions
        assertEquals(response.getStatusCode(), 200);

    }

}
