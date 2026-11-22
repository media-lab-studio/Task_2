package burgers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoginTest extends ApiTest {
    @BeforeEach
    void prepareAccount() { createUser(); }

    @Test
    void acceptsExistingUser() { checkSession(login()); }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "both"})
    void rejectsInvalidCredentials(String invalid) {
        if (!invalid.equals("password")) user.compute("email", (k, v) -> "other-" + v);
        if (!invalid.equals("email")) user.compute("password", (k, v) -> v + "-wrong");
        checkError(login(), 401, "email or password are incorrect");
    }
}
