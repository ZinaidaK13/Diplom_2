import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import request.UserRequest;
import steps.UserSteps;

public class UserRegistrationTest extends BaseAPITest {
    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("POST /api/auth/register— создание нового пользователя с валидными данными")
     public void registerValidUser(){
     UserRequest request = UserData.generateValidUser();
        Response response= UserSteps.CreateUser(request);
        UserSteps.verifyCreateUserSuccess(response, request);
    }
    @Test
    @DisplayName("Попытка создания уже зарегистрированного пользователя")
    @Description("POST /api/auth/register — регистрация с существующим email должна вернуть ошибку")
    public void registerDuplicateUser() {
        UserRequest request = UserData.generateValidUser();
        Response successResponse = UserSteps.CreateUser(request);
        UserSteps.verifyCreateUserSuccess(successResponse, request);

        Response duplicateResponse = UserSteps.CreateUser(request);
        UserSteps.verifyDuplicateUserError(duplicateResponse);
    }

    @Test
    @DisplayName("Регистрация без обязательного поля (name)")
    @Description("POST /api/auth/register — запрос без поля name должен вернуть 403 Forbidden")
    public void registerWithoutNameShouldReturn403() {
               UserRequest request = new UserRequest(
                UserData.generateEmail(),
                UserData.USER_PASSWORD,
                null);

        Response response = UserSteps.CreateUser(request);
        UserSteps.verifyDuplicateUserError(response);
    }
    @Test
    @DisplayName("Регистрация без обязательного поля (email)")
    @Description("POST /api/auth/register — запрос без поля email должен вернуть 403 Forbidden")
    public void registerWithoutEmailShouldReturn403() {
        UserRequest request = new UserRequest(
                null,
                UserData.USER_PASSWORD,
                UserData.generateName());

        Response response = UserSteps.CreateUser(request);
        UserSteps.verifyDuplicateUserError(response);
    }
    @Test
    @DisplayName("Регистрация без обязательного поля (password)")
    @Description("POST /api/auth/register — запрос без поля password должен вернуть 403 Forbidden")
    public void registerWithoutPasswordShouldReturn403() {
        UserRequest request = new UserRequest(
                UserData.generateEmail(),
                null,
                UserData.generateName());

        Response response = UserSteps.CreateUser(request);
        UserSteps.verifyDuplicateUserError(response);
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

