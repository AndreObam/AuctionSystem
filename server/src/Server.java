import java.net.InetSocketAddress;
import java.util.*;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Server extends Auction {
    private static ArrayList<User> userList = new ArrayList<User>();
    private static ArrayList<Auction> auctionList = new ArrayList<Auction>();
    private static ArrayList<Bid> bidList = new ArrayList<>();
    private static ApiResponse apiResponse = new ApiResponse();
    private static int auctionIdCounter = 0;
    private static int commandCounter = 0;

    public static void main(String[] args) throws Exception {
        System.out.print("Enter Server Port: ");
        Scanner scanner = new Scanner(System.in);
        int serverPort = scanner.nextInt();
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/availability", new SAvailabilityHandler(commandCounter));
        server.createContext("/api/user", new SUserHandler(userList, apiResponse));
        server.createContext("/api/auction", new SAuctionHandler(auctionList, bidList, apiResponse, auctionIdCounter));
        server.start();
        System.out.println("Server Is Currently Listening on Port: " + serverPort);
    }

    /*Method Used to Verify JWT Token*/
    public Boolean verifyJWT(String jwtToken) throws Exception {
        String[] output = jwtToken.split("\\.");
        String mergedHeaderPayload = output[0] + "." + output[1];

        //Check Segmentation
        if (output.length == 3) {
            System.out.println("Verified Correct Formatting and Segmenation: ");
            System.out.println("Header: " + output[0]);
            System.out.println("Payload: " + output[1]);
            System.out.println("Signature: " + output[2]);
            System.out.println("Verified Correct Segmenation");
        } else {
            System.out.println("Incorrect JWT Segmentation");
            return false;
        }

        //Check Base64Encoding
        try {
            byte[] decodedHeaderBytes = Base64.getUrlDecoder().decode(output[0].getBytes());
            String decodedHeaderStr = new String(decodedHeaderBytes);
            byte[] decodedPayloadBytes = Base64.getUrlDecoder().decode(output[1].getBytes());
            String decodedPayloadStr = new String(decodedPayloadBytes);
            System.out.println("\nDecoding JWT Token");
            System.out.println("Header Decoded: " + decodedHeaderStr);
            System.out.println("Payload Decoded: " + decodedPayloadStr);
            System.out.println("Successfully Decoded Base 64 Header and Payload");
        } catch (Exception e) {
            System.out.println("Not Base 64 Enocded");
            return false;
        }

        //Check Algorithm
        byte[] byteArray = Base64.getUrlDecoder().decode(output[0].getBytes());
        String decodedHeader = new String(byteArray);
        System.out.println("\nVerification of Algorithm:");
        System.out.println(decodedHeader);
        JSONObject jsonObject = new JSONObject(decodedHeader);
        if (jsonObject.get("alg").equals("HS256")) {
            System.out.println("Successfully Verified Algorithm - " + jsonObject.get("alg"));
        } else {
            System.out.println("Incorrect Algorithm");
            return false;
        }

        //Verification of Expiration Claim
        byte[] byteArra = Base64.getUrlDecoder().decode(output[1].getBytes());
        String decodedPayload = new String(byteArra);
        System.out.println("\nVerification of Expiration Claim:");
        System.out.println(decodedPayload);
        JSONObject jsonOb = new JSONObject(decodedPayload);
        String expString = jsonOb.get("exp").toString();
        Date curDate = new Date(System.currentTimeMillis());
        long num = Long.parseLong(expString);
        Date expDate = new Date(num);
        if (curDate.compareTo(expDate) >= 0) {
            System.out.println("Token Expired");
            return false;
        } else {
            System.out.println("Token Not Expired");
        }

        //Recalculating JWT Signature
        String secret = "secret";
        String message = mergedHeaderPayload;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String hashPadded = Base64.getUrlEncoder().encodeToString(sha256_HMAC.doFinal(message.getBytes()));
        String hash = hashPadded.replace("=", "");
        System.out.println("\nVerification of JWT Signature: ");
        System.out.println("Submitted JWT Hash: " + output[2]);
        System.out.println("Recomputed JWT Hash: " + hash);
        if (output[2].equals(hash)) {
            System.out.println("Successfully Verified JWT Signature");
            return true;
        } else {
            System.out.println("Invalid JWT Signature");
            return false;
        }
    }

    /*Method Used to Update the Amount of Commands Handled By The Server*/
    public static void setCommandCounter(int commandCounter) {
        Server.commandCounter = commandCounter;
    }

    /*Method Used to Add Auction*/
    public JSONObject addAuction(String name, float firstBid, int sellerId, String jwtToken) {
        String[] output = jwtToken.split("\\.");
        byte[] byteArray = Base64.getUrlDecoder().decode(output[1].getBytes());
        String decodedPayload = new String(byteArray);
        JSONObject jsonObject = new JSONObject(decodedPayload);
        String jwtStringId = jsonObject.get("name").toString();

        auctionIdCounter = auctionIdCounter + 1;
        Auction newAuction = new Auction();
        newAuction.setId(auctionIdCounter);
        newAuction.setName(name);
        newAuction.setFirstBid(firstBid);
        newAuction.setSellerId(sellerId);
        newAuction.setSellerName(jwtStringId);
        auctionList.add(newAuction);
        return apiResponse.setResponse(auctionIdCounter, "Available", name, firstBid, sellerId);
    }
}