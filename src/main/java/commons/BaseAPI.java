package commons;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import commons.ConfigReader;

public class BaseAPI {
    protected RequestSpecification request;

    public BaseAPI() {
        request = RestAssured.given()
                .baseUri(ConfigReader.getProperty("baseUrl"));
        // Không nên đặt contentType ở đây vì mỗi API sẽ khác nhau
    }
}
