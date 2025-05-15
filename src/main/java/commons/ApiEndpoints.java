package commons;

public class ApiEndpoints {
    public static final String LOGIN                  = "/api/users/auth-login?format=json";
    public static final String CURRENT_SESSION        = "/api/retailers/currentsession?format=json";
    public static final String WORKING_SHIFT          = "/api/workingshift";
    public static final String WORKING_SHIFT_SUMMARY  = "/api/workingshift/summary";
    public static final String WORKING_SHIFT_OPEN     = "/api/workingshift/open";
    public static final String WORKING_SHIFT_CLOSE    = "/api/workingshift/close";
    
    // Additional endpoints needed by other API classes
    public static final String RETAILER_CURRENCY      = "/api/v2/retailer/currency";
    public static final String EXCHANGE_RATES         = "/api/v2/exchange-rates";
    public static final String WORKING_SHIFT_LIST     = "/api/workingshift/list";
}
