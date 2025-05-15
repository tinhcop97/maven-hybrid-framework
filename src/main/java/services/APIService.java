package services;

import com.google.gson.Gson;
import models.LoginResponse;
import models.CurrentSessionResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import commons.Constants;

import java.io.IOException;

public class APIService {
    private static final String BASE_URL = Constants.API_BASE_URL;
    private String bearerToken;
    private String sessionId;
    private int userId;
    private int retailerId;
    private int branchId;
    private int groupId;
    private final String retailerCode;

    public APIService() {
        this.retailerCode = Constants.DEFAULT_RETAILER;
    }
    
    public APIService(String retailerCode) {
        this.retailerCode = retailerCode;
    }

    private String getResponseBody(HttpResponse response, String apiName) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = "";
        
        // Only try to read body if there is one
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            responseBody = EntityUtils.toString(entity);
        }
        
        System.out.println(apiName + " API response - Status: " + statusCode);
        System.out.println(apiName + " API response - Body: " + responseBody);
        
        if (statusCode != 200 && statusCode != 204) {
            throw new IOException(apiName + " API failed. Status: " + statusCode + ", Body: " + responseBody);
        }
        
        return responseBody;
    }

    public void authenticate(String username, String password) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(Constants.API_LOGIN_URL);
        
        // Set headers
        request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setHeader("retailer", retailerCode);
        
        // Set body
        String body = String.format("provider=credentials&UserName=%s&Password=%s&IsManageLogin=true", 
                                  username, password);
        request.setEntity(new StringEntity(body));

        // Execute request and get response body
        HttpResponse response = client.execute(request);
        String responseBody = getResponseBody(response, "Login");
        
        // Parse response
        LoginResponse loginResponse = new Gson().fromJson(responseBody, LoginResponse.class);
        
        // Store authentication data
        this.bearerToken = loginResponse.getBearerToken();
        this.sessionId = loginResponse.getSessionId();
        this.userId = loginResponse.getUserId();
        
        // Get current session info
        getCurrentSession();
    }

    private void getCurrentSession() throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(Constants.API_CURRENT_SESSION_URL);
        
        // Set headers
        request.setHeader("Authorization", "Bearer " + bearerToken);
        request.setHeader("retailer", retailerCode);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Execute request and get response body
        HttpResponse response = client.execute(request);
        String responseBody = getResponseBody(response, "Current Session");
        
        // Parse response
        CurrentSessionResponse sessionResponse = new Gson().fromJson(responseBody, CurrentSessionResponse.class);
        
        // Store session data
        this.retailerId = sessionResponse.getRetailer().getId();
        this.branchId = sessionResponse.getCurrentBranchId();
        this.groupId = sessionResponse.getRetailer().getGroupId();
    }

    public boolean hasActiveWorkingShift() throws IOException {
        // Validate we have auth token
        if (bearerToken == null) {
            throw new IllegalStateException("Must call authenticate() before checking working shift");
        }

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(Constants.API_WORKING_SHIFT_URL);
        
        // Set headers
        request.setHeader("authorization", "Bearer " + bearerToken);
        request.setHeader("branchid", String.valueOf(branchId));
        request.setHeader("content-type", "application/json;charset=UTF-8");
        request.setHeader("retailer", retailerCode);
        request.setHeader("x-app-name", Constants.APP_NAME_MHTN);
        request.setHeader("x-branch-id", String.valueOf(branchId));
        request.setHeader("x-retailer-code", retailerCode);
        request.setHeader("x-retailer-id", String.valueOf(retailerId));
        request.setHeader("x-group-id", String.valueOf(groupId));

        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        
        // Log the exact status code
        System.out.println("Working shift API response - Status: " + statusCode);
        
        // 204 means no content (no active shift)
        if (statusCode == 204) {
            System.out.println("No active shift (status code 204)");
            return false;
        }
        
        // For 200, check the response body
        String responseBody = getResponseBody(response, "Working Shift");
        System.out.println("Working shift response body: " + responseBody);
        
        // Consider empty responses or "null" string as no active shift
        boolean hasActiveShift = responseBody != null && 
                                !responseBody.trim().isEmpty() && 
                                !responseBody.equals("null") &&
                                !responseBody.equals("{}");
        
        System.out.println("Has active shift: " + hasActiveShift);
        return hasActiveShift;
    }
}
