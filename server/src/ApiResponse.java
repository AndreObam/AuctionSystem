import org.json.JSONObject;

public class ApiResponse {
    private int code;
    private String type;
    private String message;

    public ApiResponse() {
    }

    /*Method Used to Set The Reponse Code*/
    public void setCode(int responseCode) {
        this.code = responseCode;
    }

    /*Method Used to Set The Type*/
    public void setType(String responseType) {
        this.type = responseType;
    }

    /*Method Used to Set The Reponse Message*/
    public void setMessage(String responseMessage) {
        this.message = responseMessage;
    }

    /*Method Used to Retrieve the Code Value*/
    public int getCode() {
        return this.code;
    }

    /*Method Used to Retrieve the Type Value*/
    public String getType() {
        return this.type;
    }

    /*Method Used to Retrieve the Messgae*/
    public String getMessage() {
        return this.message;
    }

    /*Method Used to get the Response Value*/
    public JSONObject setResponse(int id, String status, String name, float firstBid, int sellerId) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("id", id);
        jsonResponse.put("status",status);
        jsonResponse.put("name", name);
        jsonResponse.put("firstBid",firstBid);
        jsonResponse.put("sellerId", sellerId);
        return jsonResponse;
    }

    /*Method Used to get the Response Value*/
    public JSONObject setResponse(int id, int auctionId, float bidAmount, int bidderId) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("id", id);
        jsonResponse.put("auctionId", auctionId);
        jsonResponse.put("bidAmount", bidAmount);
        jsonResponse.put("bidderId", bidderId);
        return jsonResponse;
    }

    /*Method Used to get the Response Value*/
    public JSONObject setResponse(int code, String description) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", code);
        jsonResponse.put("description",description);
        return jsonResponse;
    }
}