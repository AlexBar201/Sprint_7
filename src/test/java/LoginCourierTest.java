import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Returns the correct body and statusCode")
    @Description("Возвращается корректное тело ответа и статус код при успешном запросе")
    public void returnsTheCorrectBody(){
        creatingCourier();
        Response response = authorizationCourier();
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Authorization error with invalid login")
    @Description("Возвращается ошибка \"404 Not found\" при не верном логине")
    public void authorizationError(){
        creatingCourier();
        Response response = authorizationCourierInvalidLogin();
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Authorization error with no one field")
    @Description("Возвращается ошибка \"400 Bad Request\" когда нет одного из обязательных полей")
    public void authorizationErrorNoOneField(){
        creatingCourier();
        Response response = authorizationCourierOneField();
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Step("Передаём одно поле вместо двух обязательных")
    public Response authorizationCourierOneField(){
        CourierGetLoginData json = new CourierGetLoginData("","1234561");
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Авторизация курьера с неправильным логином")
    public Response authorizationCourierInvalidLogin(){
        CourierGetLoginData json = new CourierGetLoginData("snikers", "1234561");
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Создание курьера")
    public void creatingCourier(){
        CourierAuthorizationData json = new CourierAuthorizationData("kakaha", "1234561", "tolik");
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Авторизация курьера")
    public Response authorizationCourier(){
        CourierGetLoginData json = new CourierGetLoginData("kakaha","1234561");
        Response response = given()
                .header("Content-type","application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Получаем ID созданного курьера")
    public int getIdCourier(){
        CourierGetLoginData json = new CourierGetLoginData("kakaha","1234561");
        CouriersLogin loginID = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login")
                .as(CouriersLogin.class);
        return loginID.getId();
    }

    @Step("Удаление курьера по его ID")
    public void deleteIdCourier(){
        Response response = given()
                .header("Conrent-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + getIdCourier());
    }

    @After
    public void tearDown(){
        deleteIdCourier();
    }
}
