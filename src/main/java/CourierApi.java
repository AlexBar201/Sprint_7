import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.github.javafaker.Faker;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private final String BASE_URI;
    private final String END_POINT_COURIER;
    private final String END_POINT_COURIER_LOGIN;

    Faker faker = new Faker();
    String username = faker.name().username();
    String firstName = faker.name().firstName();
    String password = faker.internet().password(4, 8, false, false);
    String invalid_username = "snikers";
    String null_username = "";
    String invalid_password = "01010101010";
    String null_password = "";


    public CourierApi(String BASE_URI, String END_POINT_COURIER, String END_POINT_COURIER_LOGIN){
        this.BASE_URI = BASE_URI;
        this.END_POINT_COURIER = END_POINT_COURIER;
        this.END_POINT_COURIER_LOGIN = END_POINT_COURIER_LOGIN;
    }

    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Передаём два поля в теле запроса вместо трёх обязательных")
    public Response creatingCourierTwoFields(){
        CourierAuthorizationDataNoField json = new CourierAuthorizationDataNoField(username, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER);
        return response;
    }

    @Step("Создание курьера")
    public Response creatingCourier(){
        CourierAuthorizationData json = new CourierAuthorizationData(username, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER);
        return response;
    }

    @Step("Получаем ID созданного курьера")
    public int getIdCourier(){
        CourierGetLoginData json = new CourierGetLoginData(username,password);
        CouriersLogin loginID = given()
                .header("Content-type", "application/json")
                .body(json)
                .post(END_POINT_COURIER_LOGIN)
                .as(CouriersLogin.class);
        return loginID.getId();
    }

    @Step("Удаление курьера по его ID")
    public void deleteIdCourier(){
        Response response = given()
                .header("Conrent-type", "application/json")
                .when()
                .delete(END_POINT_COURIER + "/" + getIdCourier());
    }

    @Step("Авторизация курьера")
    public Response authorizationCourier(){
        CourierGetLoginData json = new CourierGetLoginData(username, password);
        Response response = given()
                .header("Content-type","application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER_LOGIN);
        return response;
    }

    @Step("Авторизация курьера с неправильным логином")
    public Response authorizationCourierInvalidLogin(){
        CourierGetLoginData json = new CourierGetLoginData(invalid_username, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER_LOGIN);
        return response;
    }

    @Step("Передаём в логин пустую строку")
    public Response authorizationCourierOneFieldLogin(){
        CourierGetLoginData json = new CourierGetLoginData(null_username, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER_LOGIN);
        return response;
    }

    @Step("Передаём в пароль пустую строку")
    public Response authorizationCourierOneFieldPassword(){
        CourierGetLoginData json = new CourierGetLoginData(username, null_password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER_LOGIN);
        return response;
    }

    @Step("Авторизация курьера с неправильным паролем")
    public Response authorizationCourierInvalidPassword(){
        CourierGetLoginData json = new CourierGetLoginData(username, invalid_password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(END_POINT_COURIER_LOGIN);
        return response;
    }


}
