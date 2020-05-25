import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.*;
import org.json.JSONObject;
/**
 * The Client Class is Used in order to Send HTTP Requests, It Inlcudes Methods That Enable GET, POST and DELETE Requests,
 * In Addition to GET, POST and DELETE Request that Inlcude Authorization Headers for JWT Purposes.
 * and Payload Values in Addition to Also Calculating the Signature.
 * @see Header
 * @see Payload
 * @author Andre Obamwonyi
 */
public class Client {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    /**
     * This Method is Used In Order to Send HTTP GET Request to a Server
     * @param url Refers to the URL address of the Server that the HTTP Request Will Be Sent To
     * @return the response in JSON format
     * @throws IOException
     */
    String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * This Method is Used In Order to Send HTTP POST Request to a Server
     * @param url Refers to the URL address of the Server that the HTTP Request Will Be Sent To
     * @return the response in JSON format
     * @throws IOException
     */
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * This Method is Used In Order to Send HTTP GET Request to a Server, This Method Also Includes the Authorization Header
     * @param url Refers to the URL address of the Server that the HTTP Request Will Be Sent To
     * @param jwtToken Refers to the JWT Token that will be sent in the Authorization Header
     * @return the response in JSON format
     * @throws IOException
     */
    String getJWT(String url, String jwtToken) throws IOException {
        Request request = new Request.Builder().url(url).get().addHeader("Authorization", "Bearer " + jwtToken).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * This Method is Used In Order to Send HTTP POST Request to a Server, This Method Also Includes the Authorization Header
     * @param url Refers to the URL address of the Server that the HTTP Request Will Be Sent To
     * @param jwtToken Refers to the JWT Token that will be sent in the Authorization Header
     * @return the response in JSON format
     * @throws IOException
     */
    String postJWT(String url, String json, String jwtToken) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).addHeader("Authorization", "Bearer " + jwtToken).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * This Method is Used In Order to Send HTTP DELETE Request to a Server, This Method Also Includes the Authorization Header
     * @param url Refers to the URL address of the Server that the HTTP Request Will Be Sent To
     * @param jwtToken Refers to the JWT Token that will be sent in the Authorization Header
     * @return the response in JSON format
     * @throws IOException
     */
    public String deleteJWT(String url, String jwtToken) throws IOException {
        Request request = new Request.Builder().url(url).delete().addHeader("Authorization", "Bearer " + jwtToken).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);
        Scanner nameScanner = new Scanner(System.in);

        String clientRequestStr = "";
        int clientRequest = 0;
        String auctionId;
        String response = "";
        String username = "";
        String password = "";
        String jwtToken = "";

        JSONObject registerCredentials = new JSONObject();
        System.out.println("--------------------");
        System.out.println("Register Credentials");
        System.out.println("--------------------");
        System.out.print("Enter Username: ");
        username = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.println("--------------------");

        registerCredentials.put("username", username);
        registerCredentials.put("password", password);
        jwtToken = client.post("http://localhost:9090/api/user", registerCredentials.toString());
        System.out.println(jwtToken);

        JSONObject loginCredentials = new JSONObject();
        System.out.println("--------------------");
        System.out.println("User Login");
        System.out.println("--------------------");
        System.out.print("Enter Username: ");
        username = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.println("--------------------");

        loginCredentials.put("username", username);
        loginCredentials.put("password", password);
        jwtToken = client.post("http://localhost:9090/api/user/login", loginCredentials.toString());
        System.out.println(jwtToken);
        System.out.println("--------------------");

        while (true) {
            try {
                System.out.println("Make a Request to Server:\n1) List All Auctions\n2) Add a New Auction\n3) Find Auction(ID)");
                System.out.println("4) Update Auction in the Store with Form Data\n5) Delete an Auction");
                System.out.println("6) Place Bid in An Auction\n7) List All Bids For An Auction ");
                System.out.print("Enter Request Number: ");
                clientRequestStr = scanner.next();
                clientRequest = Integer.parseInt(clientRequestStr);
                if (clientRequest == 1) {
                    response = client.get("http://localhost:9090/api/auctions");
                } else if (clientRequest == 2) {
                    JSONObject item = new JSONObject();
                    System.out.print("Enter Name: ");
                    item.put("name", nameScanner.nextLine());
                    System.out.print("Enter Bid: ");
                    item.put("firstBid", scanner.next());
                    System.out.print("Enter Seller ID: ");
                    item.put("sellerId", scanner.next());
                    response = client.postJWT("http://localhost:9090/api/auction", item.toString(), jwtToken);
                } else if (clientRequest == 3) {
                    System.out.print("Enter Auction ID: ");
                    auctionId = scanner.next();
                    response = client.get("http://localhost:9090/api/auction/" + auctionId);
                } else if (clientRequest == 4) {
                    System.out.print("Enter Auction ID: ");
                    auctionId = scanner.next();
                    JSONObject item = new JSONObject();
                    System.out.print("Enter Name: ");
                    item.put("name", nameScanner.nextLine());
                    System.out.print("Enter Bid: ");
                    item.put("firstBid", scanner.next());
                    System.out.print("Enter Seller ID: ");
                    item.put("sellerId", scanner.next());
                    response = client.postJWT(("http://localhost:9090/api/auction/" + auctionId), item.toString(), jwtToken);
                } else if (clientRequest == 5) {
                    System.out.print("Enter Auction ID: ");
                    auctionId = scanner.next();
                    response = client.deleteJWT(("http://localhost:9090/api/auction/" + auctionId), jwtToken);
                } else if (clientRequest == 6) {
                    JSONObject item = new JSONObject();
                    System.out.print("Enter Auction ID: ");
                    auctionId = scanner.next();
                    System.out.print("Enter Bid Amount: ");
                    item.put("bidAmount", scanner.next());
                    System.out.print("Enter Bidder ID: ");
                    item.put("bidderId", scanner.next());
                    response = client.postJWT(("http://localhost:9090/api/auction/" + auctionId + "/bid"), item.toString(), jwtToken);
                } else if (clientRequest == 7) {
                    System.out.print("Enter Auction ID: ");
                    auctionId = scanner.next();
                    response = client.getJWT(("http://localhost:9090/api/auction/" + auctionId + "/bids"), jwtToken);
                } else {
                    break;
                }
                System.out.println("--------------------");
                System.out.println(response);
                System.out.println("--------------------");
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}