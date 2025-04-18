package commons;

public class Constants {
    public static final String BASE_URL               = ConfigReader.getProperty("baseUrl");
    public static final String API_LOGIN_URL          = BASE_URL + ApiEndpoints.LOGIN;
    public static final String API_CURRENT_SESSION_URL = BASE_URL + ApiEndpoints.CURRENT_SESSION;
    public static final int TIMEOUT                   = Integer.parseInt(ConfigReader.getProperty("timeout"));
}
