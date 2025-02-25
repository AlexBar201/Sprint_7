import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersTest {

    private final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private final String END_POINT_ORDER = "/api/v1/orders";
    private final String END_POINT_ORDER_CANCEL = "/api/v1/orders/cancel";

    OrderApi orderApi = new OrderApi(BASE_URI, END_POINT_ORDER, END_POINT_ORDER_CANCEL);

    @Before
    public void before(){
        orderApi.setUp();
    }

    @Test
    @DisplayName("Returns the correct body and statusCode")
    @Description("Возвращается список заказов и статус код 200 при успешном запросе")
    public void returnsOrderList(){
        Response response = orderApi.getOrderList();
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

}
