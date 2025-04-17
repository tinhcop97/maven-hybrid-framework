package commons;

public class Constants {
    public static final String BASE_URL = ConfigReader.getProperty("baseUrl");
    public static final String API_LOGIN_URL = BASE_URL + "/api/auth/login";
    public static final int TIMEOUT = Integer.parseInt(ConfigReader.getProperty("timeout"));
}
