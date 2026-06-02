package steps;

import data.UserData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import request.OrderRequest;
import request.UserRequest;

import static io.restassured.RestAssured.given;
import static java.util.function.Predicate.not;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import data.OrderData;

public class OrderSteps {

    @Step("Отправить POST-запрос на создание заказа без авторизации")
    public static Response createOrder(String accessToken, OrderRequest request) {
        return given()
                .log().ifValidationFails()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(OrderData.ORDER_CREATE_ENDPOINT);
    }

    @Step("Проверить успешное создание заказа без авторизации")
    public static void verifyCreateOrderSuccess(Response response) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Отправить POST-запрос на создание заказа с авторизацией")
    public static Response CreateOrderAuthorized(String accessToken, OrderRequest request) {
        return given()
                .log().ifValidationFails()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(request)
                .when()
                .post(OrderData.ORDER_CREATE_ENDPOINT);
    }

    @Step("Проверить успешное создание заказа с авторизацией")
    public static void verifyCreateOrderSuccessAuthorized(Response response) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Проверить ошибку создания заказа 400")
    public static void verifyCreateOrderError400(Response response) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", containsString("Ingredient ids must be provided"));
    }
    @Step("Проверить ошибку создания заказа 500")
    public static void verifyCreateOrderError500(Response response) {
        response.then()
                .log().ifValidationFails()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body(containsString("Internal Server Error"));
    }
}
