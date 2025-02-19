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

public class CreatingCourierTest {
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Returns the correct body and statusCode")
    @Description("Возвращается корректное тело ответа и статус код при успешном запросе")
    public void returnsTheCorrectBodyTest(){
        Response response = creatingCourier();
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Creating identical couriers")
    @Description("Должна вернуться ошибка \"409 conflict\" , тело ответа \"Этот логин уже используется\"")
    public void identialCouriersTest(){
        creatingCourier();
        Response secondCourier = creatingCourier();
        secondCourier.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Bad request, no one field")
    @Description("Возвращается ошибка \"400 Bad request\" при отсутствии одного из обязательных полей в теле запроса")
    public void courierTwoFieldsBodyTest(){
        Response response = creatingCourierTwoFields();
        response.then().assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Step("Передаём два поля в теле запроса вместо трёх обязательных")
    public Response creatingCourierTwoFields(){
        CourierAuthorizationDataNoField json = new CourierAuthorizationDataNoField("Taliban","saske");
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Создание курьера")
    public Response creatingCourier(){
        CourierAuthorizationData json = new CourierAuthorizationData("Taliban", "4336", "saske");
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Получаем ID созданного курьера")
    public int getIdCourier(){
        CourierGetLoginData json = new CourierGetLoginData("Taliban","4336");
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