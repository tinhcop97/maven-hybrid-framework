package api;

import commons.Constants;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthAPI extends BaseAPI {
    
    /**
     * Đăng nhập vào hệ thống để lấy token (Phương thức instance)
     * 
     * @param retailer Mã nhà bán lẻ
     * @param username Tên người dùng
     * @param password Mật khẩu
     * @return Response từ API đăng nhập
     */
    public Response login(String retailer, String username, String password) {
        System.out.println("Sending login request to: " + Constants.API_LOGIN_URL);
        System.out.println("Using credentials - Retailer: " + retailer + ", Username: " + username + ", Password: " + password);
        
        // Try with JSON body
        Response response = given()
                .contentType("application/json")
                .header("retailer", retailer)
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post(Constants.API_LOGIN_URL)
                .then()
                .extract()
                .response();
        
        // If first attempt fails, try with form parameters
        if (response.getStatusCode() != 200) {
            System.out.println("JSON login failed with status " + response.getStatusCode() + ". Trying with form parameters...");
            response = given()
                    .contentType("application/x-www-form-urlencoded")
                    .header("retailer", retailer)
                    .formParam("username", username)
                    .formParam("password", password)
                    .formParam("provider", "credentials")
                    .formParam("IsManageLogin", "true")
                    .when()
                    .post(Constants.API_LOGIN_URL)
                    .then()
                    .extract()
                    .response();
        }
        
        return response;
    }
    
    /**
     * Đăng nhập vào hệ thống để lấy token (Phương thức static)
     * 
     * @param retailer Mã nhà bán lẻ
     * @param username Tên người dùng
     * @param password Mật khẩu
     * @return Response từ API đăng nhập
     */
    public static Response loginStatic(String retailer, String username, String password) {
        System.out.println("Sending login request to: " + Constants.API_LOGIN_URL);
        System.out.println("Using credentials - Retailer: " + retailer + ", Username: " + username + ", Password: " + password);
        
        // Try with JSON body
        Response response = given()
                .contentType("application/json")
                .header("retailer", retailer)
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post(Constants.API_LOGIN_URL)
                .then()
                .extract()
                .response();
        
        // If first attempt fails, try with form parameters
        if (response.getStatusCode() != 200) {
            System.out.println("JSON login failed with status " + response.getStatusCode() + ". Trying with form parameters...");
            response = given()
                    .contentType("application/x-www-form-urlencoded")
                    .header("retailer", retailer)
                    .formParam("username", username)
                    .formParam("password", password)
                    .formParam("provider", "credentials")
                    .formParam("IsManageLogin", "true")
                    .when()
                    .post(Constants.API_LOGIN_URL)
                    .then()
                    .extract()
                    .response();
        }
        
        return response;
    }
    
    /**
     * Đăng nhập với thông tin mặc định
     * 
     * @return Response từ API đăng nhập
     */
    public static Response loginWithDefault() {
        return loginStatic(Constants.DEFAULT_RETAILER, Constants.DEFAULT_USERNAME, Constants.DEFAULT_PASSWORD);
    }
    
    /**
     * Trích xuất token từ response đăng nhập
     * 
     * @param loginResponse Response từ API đăng nhập
     * @return Token xác thực
     */
    public static String extractToken(Response loginResponse) {
        try {
            // Kiểm tra nếu response có chứa token
            String token = loginResponse.jsonPath().getString("token");
            if (token != null && !token.isEmpty()) {
                System.out.println("Extracted token from 'token' field");
                return token;
            }
            
            // Kiểm tra nếu response có chứa BearerToken
            String bearerToken = loginResponse.jsonPath().getString("BearerToken");
            if (bearerToken != null && !bearerToken.isEmpty()) {
                System.out.println("Extracted token from 'BearerToken' field");
                return bearerToken;
            }
            
            // Kiểm tra nếu response có chứa access_token
            String accessToken = loginResponse.jsonPath().getString("access_token");
            if (accessToken != null && !accessToken.isEmpty()) {
                System.out.println("Extracted token from 'access_token' field");
                return accessToken;
            }
            
            // Nếu không tìm thấy token trong các trường thông thường
            System.out.println("No token found in response. Response body: " + loginResponse.asString());
            return null;
        } catch (Exception e) {
            System.err.println("Error extracting token: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Đăng nhập và trích xuất token
     * 
     * @param retailer Mã nhà bán lẻ
     * @return Token xác thực
     */
    public static String getToken(String retailer) {
        Response loginResponse = loginStatic(retailer, Constants.DEFAULT_USERNAME, Constants.DEFAULT_PASSWORD);
        return extractToken(loginResponse);
    }
    
    /**
     * Đăng nhập với thông tin mặc định và trích xuất token
     * 
     * @return Token xác thực
     */
    public static String getDefaultToken() {
        Response loginResponse = loginWithDefault();
        return extractToken(loginResponse);
    }
}
