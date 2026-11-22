package burgers;

import org.junit.jupiter.api.Test;

class CreateOrderTest extends ApiTest {
    @Test
    void createsUserOrder() {
        createUser();
        checkOrder(order(true, ingredients()));
    }
}
