package burgers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginTest extends ApiTest {
    @BeforeEach
    void prepareAccount() { createUser(); }

    @Test
    void acceptsExistingUser() { checkSession(login()); }
}
