import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.LoginUserRequest;
import request.UserRequest;
import steps.UserSteps;

import static data.UserData.WRONG_EMAIL;
import static data.UserData.WRONG_PASSWORD;

public class LoginUserTest extends BaseAPITest{
    private Response lastLoginResponse;
    private UserRequest userRequest;
    private Response createUserResponse;
    private String accessToken;

    @Before
    public void setUp() {
        userRequest = UserData.generateValidUser();
        createUserResponse = UserSteps.createUser(userRequest);
        UserSteps.verifyCreateUserSuccess(createUserResponse, userRequest);
    }
    @Test
    @DisplayName("Вход под существующим логином")
    @Description("POST /api/auth/login— вход под существующим логином")
    public void loginExistingUser (){
        LoginUserRequest loginRequest = new LoginUserRequest(userRequest.getEmail(), userRequest.getPassword());
        lastLoginResponse = UserSteps.loginUser(loginRequest);
        UserSteps.verifyLoginSuccess(lastLoginResponse, userRequest);

    }
    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("POST /api/auth/login — попытка входа с существующим email и неверным паролем")
    public void loginWithWrongPasswordShouldReturnError() {
              LoginUserRequest loginRequest = new LoginUserRequest(
                      userRequest.getEmail(),
                                    WRONG_PASSWORD);
        Response loginResponse = UserSteps.loginUser(loginRequest);
        UserSteps.verifyLoginError(loginResponse, loginRequest);
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("POST /api/auth/login — попытка входа с неврерным email и существующим паролем")
    public void loginWithWrongEmailShouldReturnError() {
        LoginUserRequest loginRequest = new LoginUserRequest(
                WRONG_EMAIL,
                userRequest.getPassword());
        Response loginResponse = UserSteps.loginUser(loginRequest);
        UserSteps.verifyLoginError(loginResponse, loginRequest);
    }

    @After
        public void extractAccessTokenAndCleanUp() {
        if (lastLoginResponse != null && lastLoginResponse.statusCode() == 200) {
            accessToken = lastLoginResponse.jsonPath().getString("accessToken");
            lastLoginResponse = null;
        }

        if (accessToken != null && !accessToken.isEmpty()) {
            Response deleteResponse = UserSteps.deleteUser(accessToken);
            UserSteps.verifyDeleteUserSuccess(deleteResponse);
            accessToken = null;
        }
    }
}
