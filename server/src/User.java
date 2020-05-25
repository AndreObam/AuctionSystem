public class User extends UserId {
    private String id;
    private String username;
    private String password;

    public User() {
    }

    /*Method Used set User Username*/
    public void setUsername(String setUsername) {
        this.username = setUsername;
    }

    /*Method Used To Set User Password*/
    public void setPassword(String setPassword) {
        this.password = setPassword;
    }

    /*Method Used To Retrieve User Username*/
    public String getUsername() {
        return this.username;
    }

    /*Method Used To Retrieve User Password*/
    public String getPassword() {
        return this.password;
    }

}

