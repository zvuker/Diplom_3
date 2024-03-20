package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserAccountApi {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String DELETE_USER_ENDPOINT = "/api/users";

    public void deleteUser(String userEmail) {
        RestAssured.baseURI = BASE_URL;
        Response response = RestAssured.given()
                .queryParam("email", userEmail)
                .when()
                .delete(DELETE_USER_ENDPOINT);
    }
}
