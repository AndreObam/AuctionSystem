public class UserId extends UserRequiredProperties{
    private int id;

    public UserId(){
    }
    /*Method Used To Set User Id*/
    public void setId(int setUserId){
        this.id = setUserId;
    }

    /*Method Used To Retrieve User Id*/
    public int getId(){
        return this.id;
    }
}
