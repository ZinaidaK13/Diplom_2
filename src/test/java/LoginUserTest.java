import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import request.LoginUserRequest;
import request.UserRequest;
import steps.UserSteps;

import static data.UserData.WRONG_EMAIL;
import static data.UserData.WRONG_PASSWORD;

public class LoginUserTest extends BaseAPITest{

    @Test
    @DisplayName("Вход под существующим логином")
    @Description("POST /api/auth/login— вход под существующим логином")
    public void loginExistingUser (){
        UserRequest request = UserData.generateValidUser();
        Response response= UserSteps.CreateUser(request);
        UserSteps.verifyCreateUserSuccess(response, request);

        LoginUserRequest loginRequest = new LoginUserRequest(request.getEmail(), request.getPassword());
        Response loginResponse = UserSteps.loginUser(loginRequest);

        UserSteps.verifyLoginSuccess(loginResponse, request);
        accessToken = loginResponse.jsonPath().getString("accessToken");
    }
    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("POST /api/auth/login — попытка входа с существующим email и неверным паролем")
    public void loginWithWrongPasswordShouldReturnError() {
        UserRequest request = UserData.generateValidUser();
        Response response = UserSteps.CreateUser(request);
        UserSteps.verifyCreateUserSuccess(response, request);

        LoginUserRequest loginRequest = new LoginUserRequest(
                                    request.getEmail(),
                                    WRONG_PASSWORD);
        Response loginResponse = UserSteps.loginUser(loginRequest);
        UserSteps.verifyLoginError(loginResponse, loginRequest);
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("POST /api/auth/login — попытка входа с неврерным email и существующим паролем")
    public void loginWithWrongEmailShouldReturnError() {
        UserRequest request = UserData.generateValidUser();
        Response response = UserSteps.CreateUser(request);
        UserSteps.verifyCreateUserSuccess(response, request);

        LoginUserRequest loginRequest = new LoginUserRequest(
                WRONG_EMAIL,
                request.getPassword());
        Response loginResponse = UserSteps.loginUser(loginRequest);
        UserSteps.verifyLoginError(loginResponse, loginRequest);
    }


    private String accessToken;
    @After
    public void cleanUpUser () {
        if (accessToken != null && !accessToken.isEmpty()) {
            Response deleteResponse = UserSteps.deleteUser(accessToken);
            UserSteps.verifyDeleteUserSuccess(deleteResponse);
            accessToken = null;
        }
    }
}
