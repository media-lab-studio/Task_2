package burgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.hamcrest.Matchers.is;

class ProfileTest extends ApiTest {
    @BeforeEach
    void prepareAccount() { createUser(); }

    @Step("Изменить поле {field}")
    Response update(String field, boolean authorized) {
        String value = "new-" + user.get(field);
        if (authorized) user.put(field, value);
        return api(authorized).body(Map.of(field, value)).patch("/auth/user");
    }

    @Step("Проверить ответ с обновлённым профилем")
    void checkProfile(Response response) {
        response.then().statusCode(200).body("success", is(true), "user.email", is(user.get("email")),
                "user.name", is(user.get("name")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "name", "password"})
    void updatesOneField(String field) {
        checkProfile(update(field, true));
        checkSession(login());
    }
}
