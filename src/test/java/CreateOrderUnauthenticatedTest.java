import data.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import request.OrderRequest;
import steps.OrderSteps;
import steps.UserSteps;

public class CreateOrderUnauthenticatedTest extends BaseAPITest {
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("POST /api/orders — создание заказа без авторизации")
    public void createOrderUnauthenticated() {
        OrderRequest order = OrderData.createValidOrder();
        Response response = OrderSteps.CreateOrder(null, order);
        OrderSteps.verifyCreateOrderSuccess(response);

    }
}
