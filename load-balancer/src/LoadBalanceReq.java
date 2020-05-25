import org.json.JSONObject;

public class LoadBalanceReq {
    private String requestType;
    private String url;
    private String jwtToken;
    private String json;
    private int time;

    public LoadBalanceReq(String jwtToken, String requestType, String url, String json, int time){
        this.jwtToken = jwtToken;
        this.requestType = requestType;
        this.url = url;
        this.json = json;
        this.time = time;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getUrl() {
        return url;
    }

    public String getJson() {
        return json;
    }

    public int getTime() {
        return time;
    }
}
