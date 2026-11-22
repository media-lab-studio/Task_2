package burgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.hamcrest.Matchers.is;

class CreateOrderTest extends ApiTest {
    @Test
    void createsGuestOrder() { checkOrder(order(false, ingredients())); }

    @Test
    void createsUserOrder() {
        createUser();
        var ids = ingredients();
        var response = order(true, ids);
        checkOrder(response);
        checkIngredients(response, ids);
    }

    @Step("Проверить состав созданного заказа")
    void checkIngredients(Response response, List<String> ids) {
        response.then().body("order.ingredients._id", is(ids));
    }
}
