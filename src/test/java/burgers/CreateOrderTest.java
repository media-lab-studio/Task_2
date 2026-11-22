package burgers;

import org.junit.jupiter.api.Test;

class CreateOrderTest extends ApiTest {
    @Test
    void createsGuestOrder() { checkOrder(order(false, ingredients())); }

    @Test
    void createsUserOrder() {
        createUser();
        checkOrder(order(true, ingredients()));
    }
}
