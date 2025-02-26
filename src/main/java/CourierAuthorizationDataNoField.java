public class CourierAuthorizationDataNoField {
    private String login;
    private String firstName;

    public CourierAuthorizationDataNoField(String login, String password){
        this.login = login;
        this.firstName = password;
    }

    public CourierAuthorizationDataNoField(){}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}