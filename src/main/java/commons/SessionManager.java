package commons;

import api.AuthAPI;
import api.RetailerAPI;
import com.google.gson.Gson;
import io.restassured.response.Response;
import models.CurrentSessionResponse;

/**
 * Manages session information and provides dynamic access to session data
 * such as user ID, branch ID, retailer ID, and group ID.
 */
public class SessionManager {
    private static CurrentSessionResponse currentSession;
    private static String authToken;
    private static String retailerCode;
    
    /**
     * Initializes the session by logging in and fetching the current session data.
     * This should be called at the beginning of test execution.
     */
    public static void initSession() {
        try {
            // Get credentials from config
            retailerCode = ConfigReader.getProperty("retailer");
            String username = ConfigReader.getProperty("username");
            String password = ConfigReader.getProperty("password");
            
            // Login to get auth token
            AuthAPI authAPI = new AuthAPI();
            Response loginResponse = authAPI.login(retailerCode, username, password);
            
            if (loginResponse.getStatusCode() != 200) {
                LogHelper.error("Failed to login. Status code: " + loginResponse.getStatusCode());
                LogHelper.error("Response: " + loginResponse.asString());
                return;
            }
            
            authToken = loginResponse.jsonPath().getString("BearerToken");
            if (authToken == null) {
                authToken = loginResponse.jsonPath().getString("token");
            }
            
            if (authToken == null) {
                LogHelper.error("Failed to extract auth token from login response");
                return;
            }
            
            // Get cookie from login response
            String cookie = "";
            if (!loginResponse.getCookies().isEmpty()) {
                StringBuilder cookieBuilder = new StringBuilder();
                loginResponse.getCookies().forEach((name, value) -> 
                    cookieBuilder.append(name).append("=").append(value).append("; "));
                cookie = cookieBuilder.toString();
            }
            
            // Get current session data
            RetailerAPI retailerAPI = new RetailerAPI();
            Response sessionResponse = retailerAPI.getCurrentSession(authToken, retailerCode, cookie);
            
            if (sessionResponse.getStatusCode() != 200) {
                LogHelper.error("Failed to get current session. Status code: " + sessionResponse.getStatusCode());
                LogHelper.error("Response: " + sessionResponse.asString());
                return;
            }
            
            // Parse response to model using Gson
            Gson gson = new Gson();
            currentSession = gson.fromJson(sessionResponse.asString(), CurrentSessionResponse.class);
            
            LogHelper.info("Session initialized successfully");
            LogHelper.info("Branch ID: " + getCurrentBranchId());
            LogHelper.info("Retailer ID: " + getRetailerId());
            LogHelper.info("Group ID: " + getGroupId());
            
        } catch (Exception e) {
            LogHelper.error("Error initializing session: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the current branch ID from the current session.
     * @return The branch ID or -1 if not available
     */
    public static int getCurrentBranchId() {
        return currentSession != null ? currentSession.getCurrentBranchId() : -1;
    }
    
    /**
     * Gets the retailer ID from the current session.
     * @return The retailer ID or -1 if not available
     */
    public static int getRetailerId() {
        return currentSession != null && currentSession.getRetailer() != null ? 
               currentSession.getRetailer().getId() : -1;
    }
    
    /**
     * Gets the group ID from the current session.
     * @return The group ID or -1 if not available
     */
    public static int getGroupId() {
        return currentSession != null && currentSession.getRetailer() != null ? 
               currentSession.getRetailer().getGroupId() : -1;
    }
    
    /**
     * Gets the auth token for API requests.
     * @return The auth token or null if not available
     */
    public static String getAuthToken() {
        return authToken;
    }
    
    /**
     * Gets the retailer code for API requests.
     * @return The retailer code or null if not available
     */
    public static String getRetailerCode() {
        return retailerCode;
    }
}
