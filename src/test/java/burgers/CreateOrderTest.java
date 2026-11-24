package burgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;

class CreateOrderTest extends ApiTest {
    @Test
    void rejectsInvalidIngredientHash() {
        checkServerError(order(false, List.of("invalid-id")));
    }

    @Step("Проверить ошибку сервера при неверном хеше")
    void checkServerError(Response response) {
        response.then().statusCode(500).body(containsString("Error"));
    }

    @Test
    void rejectsEmptyOrder() {
        checkError(order(false, List.of()), 400, "Ingredient ids must be provided");
    }

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
