package models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("BearerToken")
    private String bearerToken;
    
    @SerializedName("SessionId")
    private String sessionId;
    
    @SerializedName("UserId")
    private int userId;

    public String getBearerToken() {
        return bearerToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getUserId() {
        return userId;
    }
}
