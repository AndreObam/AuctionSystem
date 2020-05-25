import org.json.JSONObject;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DateFormat;
import java.util.*;
import java.security.Signature;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
/**
 * The JWT Class is Used in order to Represent the strucutre of a JWT Object, Used to Set and Rereive the Header
 * and Payload Values in Addition to Also Calculating the Signature.
 * @see Header
 * @see Payload
 * @author Andre Obamwonyi
 */
public class JWT {
    private String header;
    private String payload;
    private String signature;

    /**
     * Method Used to Return the Enocded Header Value Stored Within the JWT Object
     * @return Base64 Encoded Header Value
     */
    public String getHeader() {
        return header;
    }

    /**
     * Method Used to Return the Enocded Payload Value Stored Within the JWT Object
     * @return Base64 Encoded Payload Value
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Method Used to Return the HMAC Signature Value Stored Within the JWT Object
     * @return HMAC Signature Value
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Method Used to Set the 'Singature' Value of the JWT Object
     * @param signature Input Value used to Set the 'Signature' Value of the JWT Object
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Method Used to Set the 'Payload' Value of the JWT Object
     * @param payload Input Value used to Set the 'Payload' Value of the JWT Object
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * Method Used to Set the 'Header' Value of the JWT Object
     * @param header Input Value used to Set the 'Header' Value of the JWT Object
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Method Used to Return the JSON Representation of the JWT Object
     * @return JSON Representation of JWT Object
     */
    public JSONObject getJSON(){
        JSONObject jwt = new JSONObject();
        jwt.put("header", header);
        jwt.put("payload",payload);
        jwt.put("signature",signature);
        return jwt;
    }

    /**
     * Constructor used to Initialzie the Variable Values Used for the JWT Token, Sets the Unique Identifiers (Name and Id)
     * Of the Users Through the Given Parameters, Thus every JWT Token Has a Unique Name and ID.
     * @param username is the Value used to refer to the owner of the Token
     * @param id is an alternative value used to refer to the owner of the token
     * @throws Exception
     */
    public JWT (String username, int id) throws Exception{
        JSONObject headerObject = new JSONObject();
        JSONObject payloadObject = new JSONObject();

        /*Creation of Issued at Time and Expiration Claim Data*/
        Date date = new Date(System.currentTimeMillis());
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tomDate = cal.getTime();

        /*Creation of Header and Payload Objects*/
        headerObject.put("typ", "JWT");
        headerObject.put("alg","HS256");
        payloadObject.put("name",username);
        payloadObject.put("id",id);
        payloadObject.put("iat", date.getTime());
        payloadObject.put("exp", tomDate.getTime());

        /*Base64 Encoding Process*/
        String encodedHeader = Base64.getUrlEncoder().encodeToString(headerObject.toString().getBytes());
        String encodedPayloadPadded = Base64.getUrlEncoder().encodeToString(payloadObject.toString().getBytes());
        String encodedPayload = encodedPayloadPadded.replace("=", "");

        /*Signature Creation*/
        try {
            String secret = "secret";
            String message = encodedHeader + "." + encodedPayload;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hashPadded = Base64.getUrlEncoder().encodeToString(sha256_HMAC.doFinal(message.getBytes()));
            String hash = hashPadded.replaceAll("=","");

            setHeader(encodedHeader);
            setPayload(encodedPayload);
            setSignature(hash);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}