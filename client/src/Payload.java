/**
 * The Payload Class is Used in order to Represent the Payload of a JWT Object, Used to Set and Retrieve Claim Values
 * @see JWT
 * @author Andre Obamwonyi
 */
public class Payload {
    private String name;
    private int id;
    private long iat;
    private long exp;
    /**
     * Constructor Initialises the Variables of the of the Payload, Setting All Of Required Claim Values
     * @param name Refers to the Name of the Token Owner, Value used to Identify Owner of the Token
     * @param id Refers to the Id of the Token Owner, Value used to Identify Owner of the Token
     * @param iat Refers to the Issued at Time Claim, Value Represents the Time of Which the Token was Issued
     * @param exp Refers to the Expiration Claim, Value Represents the Time in Which the Token Expires
     */
    public Payload(String name, int id, long iat, long exp){
        this.name = name;
        this.id = id;
        this.iat = iat;
        this.exp = exp;
    }

    /**
     * Method Used to Set the Custom 'Iat' Value Claim Stored Within the JWT Payload
     * @param name Input Value used to Set the Custom 'name' Claim Value, Refers to the Owner of the Token
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method Used to Set the Custom 'Id' Value Claim Stored Within the JWT Payload
     * @param id Input Value used to Set the Custom 'id' Claim Value, Alternative Value Refers to the Owner of the Token
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method Used to Set the 'Iat' Value Claim Stored Within the JWT Payload
     * @param iat Input Value used to Set the 'Iat' Claim Value, Refers to the Time of Which the Token was Issued
     */
    public void setIat(long iat) {
        this.iat = iat;
    }

    /**
     * Method Used to Set the 'Exp' Claim Stored Within the JWT Payload
     * @param exp Input Value used to Set the 'Exp' Claim Value, Time in Which the Token Expires
     */
    public void setExp(long exp) {
        this.exp = exp;
    }

    /**
     * Method Used to Return the Custom 'Name' Claim Stored Within the JWT Payload
     * @return Custom 'Name' Claim Value
     */
    public String getName() {
        return name;
    }

    /**
     * Method Used to Return the Custom 'Id' Claim Stored Within the JWT Payload
     * @return Custom 'id' Claim Value
     */
    public int getId() {
        return id;
    }

    /**
     * Method Used to Return the 'Issued At Time' Claim Stored Within the JWT Payload
     * @return 'Issued at Time (Iat)' Claim Value
     */
    public long getIat() {
        return iat;
    }

    /**
     * Method Used to Return the Expiration Claim Stored Within the JWT Payload
     * @return Expiration Claim Value
     */
    public long getExp() {
        return exp;
    }
}