package burgers;

import org.junit.jupiter.api.Test;

class RegisterTest extends ApiTest {
    @Test
    void registersUniqueUser() { checkSession(register()); }

    @Test
    void rejectsRegisteredUser() {
        createUser();
        checkError(register(), 403, "User already exists");
    }
}
