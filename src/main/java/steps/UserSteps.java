package steps;
import io.qameta.allure.Step;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import request.LoginUserRequest;
import request.UserRequest;
import data.UserData;


import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class UserSteps {

    @Step("Отправить POST-запрос на создание пользователя")
    public static Response CreateUser(UserRequest request) {
        return given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(UserData.USER_CREATE_ENDPOINT);
    }

    @Step("Проверить успешный ответ: статус 200, поле ok = true")
    public static void verifyCreateUserSuccess(Response response, UserRequest expectedUser) {
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(expectedUser.getName()));
    }

    @Step("Проверить ошибку 403, Forbidden")
    public static void verifyDuplicateUserError(Response response) {
        response.then()
                .log().all()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Step("Удалить пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken) // Bearer токен (уже с префиксом)
                .when()
                .delete(UserData.USER_DELETE_ENDPOINT);
    }

    @Step("Проверить успешное удаление пользователя (202)")
    public static void verifyDeleteUserSuccess(Response response) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true));

    }

    @Step("Отправить POST-запрос на авторизацию пользователя")
    public static Response loginUser(LoginUserRequest request) {
        return given()
                .log().ifValidationFails()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(UserData.USER_LOGIN_ENDPOINT);

    }

    @Step("Проверить успешный ответ авторизации")
    public static void verifyLoginSuccess(Response response, UserRequest expectedUser) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(expectedUser.getName()));

    }

    @Step("Проверить ошибку авторизации (неверные учётные данные)")
    public static void verifyLoginError(Response response, LoginUserRequest request) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));

    }
}
