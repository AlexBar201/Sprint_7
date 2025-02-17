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
    public void returnsTheCorrectBody(){
        CourierAuthorizationData json = new CourierAuthorizationData("Taliban", "4336", "saske");

        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @After
    public  void deleteCourier(){
        CourierGetLoginData json = new CourierGetLoginData("Taliban", "4336");

        CouriersLogin loginID = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login")
                .body()
                .as(CouriersLogin.class);

        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + loginID.getId());

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }
}
