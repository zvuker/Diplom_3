package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import com.google.gson.Gson;

public class UserRegistrationApi {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTER_ENDPOINT = "/api/auth/register";

    public static void registerUser(String userName, String userEmail, String userPassword) {
        RestAssured.baseURI = BASE_URL;
        UserRegistrationRequest request = new UserRegistrationRequest(userName, userEmail, userPassword);
        Gson gson = new Gson();
        String requestBody = gson.toJson(request);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(REGISTER_ENDPOINT);
    }

    public void deleteUser(String userEmail) {
    }

    private static class UserRegistrationRequest {
        private String name;
        private String email;
        private String password;

        public UserRegistrationRequest(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}
