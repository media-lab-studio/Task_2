package burgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

class UserOrdersTest extends ApiTest {
    @Test
    void rejectsAnonymousHistory() {
        checkError(orders(false), 401, "You should be authorised");
    }

    @Step("Запросить список заказов")
    Response orders(boolean authorized) { return api(authorized).get("/orders"); }

    @Step("Проверить заказы нового пользователя")
    void checkHistory(Response response, int number) {
        response.then().statusCode(200).body("success", is(true), "orders.number", contains(number));
    }

    @Test
    void returnsOwnOrder() {
        createUser();
        int number = checkOrder(order(true, ingredients()));
        checkHistory(orders(true), number);
    }
}
