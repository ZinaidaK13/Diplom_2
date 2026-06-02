import data.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.OrderRequest;
import request.UserRequest;
import steps.OrderSteps;
import steps.UserSteps;

public class AuthorizedOrderCreationTest  extends BaseAPITest{
    private String accessToken;
    @Before
    public void setUpAuth() {
        UserRequest user = data.UserData.generateValidUser();
        UserSteps.createUser(user);

        request.LoginUserRequest loginReq = new request.LoginUserRequest(user.getEmail(),data.UserData.USER_PASSWORD);
        Response loginRes = UserSteps.loginUser(loginReq);
        accessToken = loginRes.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("POST /api/orders — успешное создание заказа с токеном")
    public void createOrderWithAuthAndIngredientsShouldReturn200() {
        OrderRequest order = OrderData.createValidOrder();
        Response response = OrderSteps.CreateOrderAuthorized(accessToken, order);
        OrderSteps.verifyCreateOrderSuccessAuthorized(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("POST /api/orders — пустой список ингредиентов должен вернуть 400")
    public void createOrderWithoutIngredientsShouldReturn400() {
        OrderRequest order = OrderData.createEmptyOrder();
        Response response = OrderSteps.createOrder(accessToken, order);
        OrderSteps.verifyCreateOrderError400(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("POST /api/orders — невалидный ID ингредиента должен вернуть 500")
    public void createOrderWithInvalidIngredientHashShouldReturn500() {
        OrderRequest order = OrderData.createInvalidOrder();
        Response response = OrderSteps.createOrder(accessToken, order);
        OrderSteps.verifyCreateOrderError500(response);
    }

    @After
    public void cleanUpUser() {
        if (accessToken != null) {
            try { UserSteps.deleteUser(accessToken); }
            catch (Exception ignored) {}
        }
    }

}











