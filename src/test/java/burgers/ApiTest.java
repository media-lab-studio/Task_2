package burgers;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

abstract class ApiTest {
    final Map<String, String> user = new HashMap<>(Map.of(
            "email", UUID.randomUUID() + "@example.org", "password", UUID.randomUUID().toString(), "name", "Тестер"));
    private String token;

    RequestSpecification api(boolean authorized) {
        var request = given().baseUri(System.getProperty("api.url", "https://stellarburgers.education-services.ru"))
                .basePath("/api").contentType(ContentType.JSON);
        return authorized ? request.header("Authorization", token) : request;
    }

    @Step("Отправить запрос регистрации")
    Response register() {
        var response = api(false).body(user).post("/auth/register");
        if (response.statusCode() == 200) token = response.path("accessToken");
        return response;
    }

    @Step("Подготовить зарегистрированного пользователя")
    void createUser() { checkSession(register()); }

    @Step("Проверить профиль и выдачу токенов")
    void checkSession(Response response) {
        response.then().statusCode(200).body("success", is(true), "user.email", is(user.get("email")),
                "user.name", is(user.get("name")), "accessToken", matchesPattern("Bearer \\S+"),
                "refreshToken", not(emptyOrNullString()));
    }

    @Step("Проверить код и сообщение об ошибке")
    void checkError(Response response, int status, String message) {
        response.then().statusCode(status).body("success", is(false), "message", is(message));
    }

    @Step("Войти с текущими данными")
    Response login() {
        return api(false).body(Map.of("email", user.get("email"), "password", user.get("password"))).post("/auth/login");
    }

    @Step("Получить ингредиент из каталога")
    List<String> ingredients() {
        String id = api(false).get("/ingredients").then().statusCode(200)
                .body("success", is(true), "data[0]._id", not(emptyOrNullString())).extract().path("data[0]._id");
        return List.of(id);
    }

    @Step("Отправить заказ (авторизация: {authorized})")
    Response order(boolean authorized, List<String> ids) {
        return api(authorized).body(Map.of("ingredients", ids)).post("/orders");
    }

    @Step("Проверить создание заказа и получить его номер")
    int checkOrder(Response response) {
        return response.then().statusCode(200).body("success", is(true), "name", not(emptyOrNullString()),
                "order.number", greaterThan(0)).extract().path("order.number");
    }

    @AfterEach
    @Step("Удалить созданного пользователя")
    void cleanUp() {
        if (token != null) api(true).delete("/auth/user").then().statusCode(202).body("success", is(true));
    }
}
