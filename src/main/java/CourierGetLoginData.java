public class CourierGetLoginData {
    private String login;
    private String password;

    public CourierGetLoginData(String login, String password){
        this.login = login;
        this.password = password;
    }

    public CourierGetLoginData(){}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
