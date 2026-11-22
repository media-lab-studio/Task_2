package burgers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RegisterTest extends ApiTest {
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "name"})
    void rejectsMissingField(String field) {
        user.remove(field);
        checkError(register(), 403, "Email, password and name are required fields");
    }

    @Test
    void registersUniqueUser() { checkSession(register()); }

    @Test
    void rejectsRegisteredUser() {
        createUser();
        checkError(register(), 403, "User already exists");
    }
}
