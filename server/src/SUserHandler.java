import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SUserHandler implements HttpHandler {
    private ArrayList<User> userList = new ArrayList<User>();
    private ApiResponse apiResponse = new ApiResponse();

    public SUserHandler(ArrayList<User> userList, ApiResponse apiResponse){
        this.userList = userList;
        this.apiResponse = apiResponse;
    }

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        is.read();
        t.getResponseHeaders().set("Content-Type", "application/json");
        String endpoint = t.getRequestURI().toString();
        JSONArray jsonArrayResponse;
        JSONObject jsonObjectResponse;
        String response = "This is the response";

        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        /*Method Used to Handle Register User Endpoint Requests*/
        if (endpoint.equals("/api/user")) {
            try {
                System.out.println("----------------------------------------------------");
                System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
                String postString = buf.toString();
                Pattern pattern = Pattern.compile("([a-zA-Z\":\"]{12})([0-9a-zA-Z:+]+)(\",\")([0-9a-zA-Z:\"+]{11})([0-9a-zA-Z]+)");
                Matcher matcher = pattern.matcher(postString);
                String username = "";
                String password = "";
                while (matcher.find()) {
                    username = matcher.group(5);
                    password = matcher.group(2);
                }
                jsonObjectResponse = createUser(username, password);
                response = jsonObjectResponse.toString();
                if (response.contains("payload")) {
                    response = (jsonObjectResponse.get("header").toString() + "." + jsonObjectResponse.get("payload").toString() + "." + jsonObjectResponse.get("signature").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*Method Used to Handle Login Endpoint Requests*/
        else if (endpoint.equals("/api/user/login")) {
            try {
                System.out.println("----------------------------------------------------");
                System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
                String postString = buf.toString();
                Pattern pattern = Pattern.compile("([a-zA-Z\":\"]{12})([0-9a-zA-Z:+]+)(\",\")([0-9a-zA-Z:\"+]{11})([0-9a-zA-Z]+)");
                Matcher matcher = pattern.matcher(postString);
                String username = "";
                String password = "";
                while (matcher.find()) {
                    username = matcher.group(5);
                    password = matcher.group(2);
                }
                jsonObjectResponse = loginUser(username, password);
                response = jsonObjectResponse.toString();
                if (response.contains("payload")) {
                    response = (jsonObjectResponse.get("header").toString() + "." + jsonObjectResponse.get("payload").toString() + "." + jsonObjectResponse.get("signature").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (endpoint.equals("/api/user/availability")) {
            System.out.println("Server Check");
            response = "Up";
        }
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /*Method Used To Create User*/
    public JSONObject createUser(String username, String password) throws Exception {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        for (User userListing : userList) {
            if (userListing.getUsername().equals(username)) {
                return apiResponse.setResponse(400, "Invalid username/password supplied");
            }
        }
        if (username.matches("[A-Za-z0-9]+")) {
            userList.add(newUser);
            JWT newJWT = new JWT(username, userList.size());
            JSONObject jwtJSON = newJWT.getJSON();
            return jwtJSON;
        } else {
            return apiResponse.setResponse(400, "Invalid username/password supplied");
        }
    }

    /*Method Used To Login User*/
    public JSONObject loginUser(String username, String password) throws Exception {
        User newLogUser = new User();
        for (User userListing : userList) {
            if (userListing.getUsername().equals(username)) {
                newLogUser = userListing;
                if (newLogUser.getPassword().equals(password)) {
                    JWT newJWT = new JWT(username, userList.size());
                    JSONObject jwtJSON = newJWT.getJSON();
                    return jwtJSON;
                } else {
                    return apiResponse.setResponse(400, "Invalid username/password supplied");
                }
            }
        }
        return apiResponse.setResponse(400, "Invalid username/password supplied");
    }
}