/**
 * The Header Class is Used in order to Represent the Header of a JWT Object, Used to Set and Retrieve Header Fields
 * @see JWT
 * @author Andre Obamwonyi
 */
public class Header {
    String type;
    String alg;
    /**
     * Constructor Initialises the Variables of the of the Header, Setting All Of Required Header Values
     * @param type Refers to the Object Identifier, All JWT Objects Have a Type of JWT
     * @param alg Refers to the Encryption Algorithm Used to Encrypt the JWT Token
     */
    public Header(String type, String alg){
        this.type = type;
        this.alg = alg;
    }

    /**
     * Method Used to Set the 'type' Header Value Stored Within the JWT Header
     * @param headerType Input Value used to Set the 'typ' Header Value, Refers to the type of object being sent
     */
    public void setType(String headerType){
        this.type = headerType;
    }

    /**
     * Method Used to Set the 'alg' Header Value Stored Within the JWT Header
     * @param headerAlg Input Value used to Set the 'alg' Header Value, Refers to the Encryption Algorithim of the JWT
     */
    public void setAlg(String headerAlg){
        this.alg = headerAlg;
    }

    /**
     * Method Used to Return the 'alg' Header Value Stored Within the JWT Header
     * @return 'alg' Header Value
     */
    public String getAlg() {
        return alg;
    }

    /**
     * Method Used to Return the 'type' Header Value Stored Within the JWT Header
     * @return 'type' Header Value
     */
    public String getType() {
        return type;
    }
}
