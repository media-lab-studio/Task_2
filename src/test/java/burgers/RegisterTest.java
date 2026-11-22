package burgers;

import org.junit.jupiter.api.Test;

class RegisterTest extends ApiTest {
    @Test
    void registersUniqueUser() { checkSession(register()); }
}
