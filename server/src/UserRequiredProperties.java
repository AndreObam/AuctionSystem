public class UserRequiredProperties {
    private int id;
    private String username;
    private String password;

    public UserRequiredProperties(){ }

    /*Method Used To Set User Id*/
    public void setId(int setId){
        this.id = setId;
    }

    /*Method Used To Retrieve User Username*/
    public void setUsername(String setUsername){
        this.username = setUsername;
    }

    /*Method Used To Set User Password*/
    public void setPassword(String setPassword){
        this.password = setPassword;
    }

    /*Method Used To Retrieve User Username*/
    public String getUsername(){
        return this.username;
    }

    /*Method Used To get User Password*/
    public String getPassword(){
        return this.password;
    }

    /*Method Used To Retrieve User Id*/
    public int getId(){
        return this.id;
    }
}
