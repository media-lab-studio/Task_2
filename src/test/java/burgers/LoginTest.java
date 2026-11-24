package burgers;

import io.qameta.allure.Step;
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
        prepareInvalidCredentials(invalid);
        checkError(login(), 401, "email or password are incorrect");
    }

    @Step("Подготовить неверные данные для входа: {invalid}")
    void prepareInvalidCredentials(String invalid) {
        if (!invalid.equals("password")) user.compute("email", (k, v) -> "other-" + v);
        if (!invalid.equals("email")) user.compute("password", (k, v) -> v + "-wrong");
    }
}
