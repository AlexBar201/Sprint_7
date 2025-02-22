import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersTest {
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Returns the correct body and statusCode")
    @Description("Возвращается список заказов и статус код 200 при успешном запросе")
    public void returnsOrderList(){
        Response response = getOrderList();
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Получение списка заказов")
    public Response getOrderList(){
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        return response;
    }
}
