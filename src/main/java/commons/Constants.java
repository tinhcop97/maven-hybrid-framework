package commons;

public class Constants {
    // API URLs
    public static final String BASE_URL = normalizeUrl(ConfigReader.getProperty("baseUrl"));
    public static final String API_BASE_URL = BASE_URL;
    public static final String API_LOGIN_URL = BASE_URL + ApiEndpoints.LOGIN;
    public static final String API_CURRENT_SESSION_URL = BASE_URL + ApiEndpoints.CURRENT_SESSION;
    public static final String API_WORKING_SHIFT_URL = BASE_URL + ApiEndpoints.WORKING_SHIFT;
    public static final String API_WORKING_SHIFT_SUMMARY_URL = BASE_URL + ApiEndpoints.WORKING_SHIFT_SUMMARY;
    public static final String API_WORKING_SHIFT_OPEN_URL = BASE_URL + ApiEndpoints.WORKING_SHIFT_OPEN;
    public static final String API_WORKING_SHIFT_CLOSE_URL = BASE_URL + ApiEndpoints.WORKING_SHIFT_CLOSE;
    public static final String API_RETAILER_CURRENCY_URL = BASE_URL + ApiEndpoints.RETAILER_CURRENCY;
    public static final String API_EXCHANGE_RATES_URL = BASE_URL + ApiEndpoints.EXCHANGE_RATES;
    public static final String API_WORKING_SHIFT_LIST_URL = BASE_URL + ApiEndpoints.WORKING_SHIFT_LIST;
    
    // Default values from config.properties
    public static final String DEFAULT_RETAILER = ConfigReader.getProperty("retailer");
    public static final String DEFAULT_USERNAME = ConfigReader.getProperty("username");
    public static final String DEFAULT_PASSWORD = ConfigReader.getProperty("password");
    
    // Dynamic values from current session - these will be initialized by SessionManager
    public static String getBranchId() {
        int branchId = SessionManager.getCurrentBranchId();
        return branchId > 0 ? String.valueOf(branchId) : "5"; // Fallback to default if not initialized
    }
    
    public static String getRetailerId() {
        int retailerId = SessionManager.getRetailerId();
        return retailerId > 0 ? String.valueOf(retailerId) : "5"; // Fallback to default if not initialized
    }
    
    public static String getGroupId() {
        int groupId = SessionManager.getGroupId();
        return groupId > 0 ? String.valueOf(groupId) : "1"; // Fallback to default if not initialized
    }
    
    // Default value for cashier user ID (should be obtained from current session in future)
    public static final int DEFAULT_CASHIER_USER_ID = 10;
    
    // Application constants
    public static final String APP_NAME_MHTN = "web-pos";
    public static final String APP_NAME_MHQL = "web-man";
    public static final String ROUNDING_HALF_EVEN = "HalfEven";
    public static final String ROUNDING_HALF_UP = "HalfUp";
    public static final String ROUNDING_FLOOR = "Floor";
    public static final int TIMEOUT = Integer.parseInt(ConfigReader.getProperty("timeout"));
    
    // Default rounding rule
    public static String getRoundingRule() {
        return ROUNDING_HALF_UP; // Default to HALF_UP rounding
    }
    
    // Normalize URL to avoid double slashes when concatenating URLs
    // @param url URL to normalize
    // @return Normalized URL without trailing slash
    private static String normalizeUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.endsWith("/") ? url : url + "/";
    }
}
