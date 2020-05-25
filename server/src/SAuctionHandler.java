import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SAuctionHandler extends Server implements HttpHandler {
    private ArrayList<Auction> auctionList = new ArrayList<Auction>();
    private ArrayList<Bid> bidList = new ArrayList<Bid>();
    private ApiResponse apiResponse = new ApiResponse();
    private int auctionIdCounter = 0;

    public SAuctionHandler(ArrayList<Auction> auctionList, ArrayList<Bid> bidList, ApiResponse apiResponse, int auctionIdCounter){
        this.auctionList = auctionList;
        this.bidList = bidList;
        this.apiResponse = apiResponse;
        this.auctionIdCounter = auctionIdCounter;
    }

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        is.read();
        t.getResponseHeaders().set("Content-Type", "application/json");
        String requestMethod = t.getRequestMethod();
        String endpoint = t.getRequestURI().toString();
        JSONArray jsonArrayResponse;
        String response = "This is the response";

        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        /*List All Auctions - GET*/
        if (endpoint.equals("/api/auctions")) {
            System.out.println("----------------------------------------------------");
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            jsonArrayResponse = listAuctions();
            response = jsonArrayResponse.toString();
        }

        /*Delete An Auction By ID* - DELETE*/
        else if (endpoint.contains("/api/auction/") && requestMethod.equals("DELETE")) {
            System.out.println("----------------------------------------------------");
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            List<String> jwtList = t.getRequestHeaders().get("Authorization");
            String jwtToken = jwtList.toString();
            jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
            try {
                int auctionId = Integer.parseInt(endpoint.substring(13));
                response = deleteAuction(auctionId, jwtToken).toString();
            } catch (Exception e) {
                response = apiResponse.setResponse(400, "Invalid Input").toString();
            }
        }

        /*Add A New Auction - POST*/
        else if (endpoint.equals("/api/auction")) {
            try {
                System.out.println("----------------------------------------------------");
                System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
                List<String> authorizationToken = t.getRequestHeaders().get("Authorization");
                String myString = authorizationToken.toString();
                myString = myString.substring(0, myString.length() - 1).replaceAll("\\[Bearer ", "");
                if (verifyJWT(myString)) {
                    try {
                        String postString = buf.toString();
                        Pattern pattern = Pattern.compile("([a-zA-Z\":]{11})([0-9]+)([a-zA-Z\",:]{14})([a-zA-Z\\s-]+)([a-zA-Z\",:]{12})([0-9]+)");
                        Matcher matcher = pattern.matcher(postString);
                        String postName = "";
                        String postBid = "";
                        String postId = "";
                        while (matcher.find()) {
                            postBid = matcher.group(2);
                            postName = matcher.group(4);
                            postId = matcher.group(6);
                        }
                        response = addAuction(postName, Float.parseFloat(postBid), Integer.parseInt(postId), myString).toString();
                    } catch (Exception e) {
                        response = apiResponse.setResponse(400, "Invalid Input").toString();
                    }
                } else {
                    response = apiResponse.setResponse(403, "Unauthorized Token").toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*Find An Auction By ID* - GET*/
        else if (endpoint.contains("/api/auction/") && requestMethod.equals("GET")) {
            try {
                System.out.println("----------------------------------------------------");
                System.out.println("Methods: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
                int auctionId = Integer.parseInt(endpoint.substring(13));
                response = findAuction(auctionId).toString();
            } catch (Exception e) {
                response = apiResponse.setResponse(400, "Invalid ID Supplied").toString();
            }
        }

        /*Update Auction By ID* - POST*/
        else if (endpoint.contains("/api/auction") && requestMethod.equals("POST")) {
            System.out.println("----------------------------------------------------");
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            List<String> authorizationToken = t.getRequestHeaders().get("Authorization");
            String myString = authorizationToken.toString();
            myString = myString.substring(0, myString.length() - 1).replaceAll("\\[Bearer ", "");
            try {
                if (verifyJWT(myString)) {
                    try {
                        String postString = buf.toString();
                        int auctionId = Integer.parseInt(endpoint.substring(13));
                        Pattern pattern = Pattern.compile("([a-zA-Z\":,]{11})([0-9]+)([a-zA-Z\":,]{14})([a-zA-Z]+)([a-zA-Z\":,]{12})([0-9]+)");
                        Matcher matcher = pattern.matcher(postString);
                        String postName = "";
                        String postBid = "";
                        String postId = "";
                        while (matcher.find()) {
                            postName = matcher.group(4);
                            postBid = matcher.group(2);
                            postId = matcher.group(6);
                        }
                        response = updateAuction(auctionId, postName, Float.parseFloat(postBid), Integer.parseInt(postId)).toString();
                    } catch (Exception e) {
                        response = apiResponse.setResponse(400, "Invalid Input").toString();
                    }
                } else {
                    response = apiResponse.setResponse(403, "Unauthorized, this is not your auction").toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*Place Bid*/
        if (endpoint.contains("/api/auction/") && endpoint.contains("/bid") && requestMethod.equals("POST")) {
            System.out.println("----------------------------------------------------");
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            List<String> authorizationToken = t.getRequestHeaders().get("Authorization");
            String myString = authorizationToken.toString();
            myString = myString.substring(0, myString.length() - 1).replaceAll("\\[Bearer ", "");

            System.out.println("\n\n\n" + buf.toString());
            try {
                if (verifyJWT(myString)) {
                    try {
                        String strAuctionId = ((endpoint.substring(13)).substring(0, (endpoint.substring(13)).indexOf('/')));
                        int auctionId = Integer.parseInt(strAuctionId);
                        String postString = buf.toString();
                        Pattern pattern = Pattern.compile("(\"[a-zA-Z:\"]{12})([0-9]+)([a-zA-Z,:\"]{14})([0-9]+)");
                        Matcher matcher = pattern.matcher(postString);
                        String bidAmount = "";
                        String bidderId = "";
                        while (matcher.find()) {

                            bidAmount = matcher.group(2);
                            bidderId = matcher.group(4);
                            System.out.println("Auction Id: " + auctionId);
                            System.out.println("Bid Amount: " + bidAmount);
                            System.out.println("Bidder Id: " + bidderId);
                        }
                        response = placeBid(auctionId, Float.parseFloat(bidAmount), Integer.parseInt(bidderId), myString).toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        response = apiResponse.setResponse(400, "Invalid Input").toString();
                    }
                } else {
                    response = apiResponse.setResponse(403, "Unauthorized Token").toString();
                }
            } catch (Exception e) {
                response = apiResponse.setResponse(405, "Error During Bid Process").toString();
            }
        }

        /*List All Bid*/
        else if (endpoint.contains("/api/auction/") && endpoint.contains("/bids") && requestMethod.equals("GET")) {
            List<String> jwtList = t.getRequestHeaders().get("Authorization");
            String jwtToken = jwtList.toString();
            jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
            System.out.println("----------------------------------------------------");
            System.out.println("Methodssss: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            try {
                if (verifyJWT(jwtToken)) {
                    try {
                        int auctionId = Integer.parseInt((endpoint.substring(13)).substring(0, (endpoint.substring(13)).indexOf('/')));
                        jsonArrayResponse = listBids(auctionId);
                        response = jsonArrayResponse.toString();
                    } catch (Exception e) {
                        response = apiResponse.setResponse(400, "Invalid Input").toString();
                    }
                } else {
                    response = apiResponse.setResponse(403, "Unauthorized Token").toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

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

    public JSONArray listAuctions() {
        JSONArray jsonResponseFinal = new JSONArray();
        JSONObject jsonResponse = new JSONObject();
        for (int i = 0; i < auctionList.size(); i++) {
            jsonResponse = apiResponse.setResponse(auctionList.get(i).getId(),auctionList.get(i).getStatus(),
                    auctionList.get(i).getName(), auctionList.get(i).getFirstBid(), auctionList.get(i).getSellerId());
            jsonResponseFinal.put(jsonResponse);
        }
        return jsonResponseFinal;
    }

    public JSONObject findAuction(int auctionId) {
        JSONObject jsonResponse = new JSONObject();
        for (Auction auctionListing : auctionList) {
            if (auctionListing.getId() == auctionId) {
                return apiResponse.setResponse(auctionListing.getId(), auctionListing.getStatus(),
                        auctionListing.getName(), auctionListing.getFirstBid(), auctionListing.getSellerId());
            }
        }
        return apiResponse.setResponse(404, "Auction not found");
    }

    public JSONObject deleteAuction(int auctionId, String jwtToken) {
        String[] output = jwtToken.split("\\.");
        byte[] byteArray = Base64.getUrlDecoder().decode(output[1].getBytes());
        String decodedPayload = new String(byteArray);
        JSONObject jsonObject = new JSONObject(decodedPayload);
        String jwtStringName = jsonObject.get("name").toString();
        for (Auction auctionListing : auctionList) {
            if (auctionListing.getId() == auctionId) {
                if (auctionListing.getSellerName().equals(jwtStringName)) {
                    auctionList.remove(auctionListing);
                    return apiResponse.setResponse(200, "Successful operation");
                } else {
                    return apiResponse.setResponse(403, "Unauthorized, Not Your Auction");
                }
            }
        }
        return apiResponse.setResponse(404, "Auction not found");
    }

    public JSONObject updateAuction(int auctionId, String name, float firstBid, int sellerId) {
        for (Auction auctionListing : auctionList) {
            if (auctionListing.getId() == auctionId) {
                auctionListing.setName(name);
                auctionListing.setFirstBid(firstBid);
                auctionListing.setSellerId(sellerId);
                return apiResponse.setResponse(auctionId, "Available", name, firstBid, sellerId);
            }
        }
        return apiResponse.setResponse(404, "Auction Not Found");
    }


    public JSONObject placeBid(int auctionId, float bidAmount, int bidderId, String jwtToken) {
        String[] output = jwtToken.split("\\.");
        byte[] byteArray = Base64.getUrlDecoder().decode(output[1].getBytes());
        String decodedPayload = new String(byteArray);
        JSONObject jsonObject = new JSONObject(decodedPayload);
        String jwtStringName = jsonObject.get("name").toString();
        float highestBid = 0;
        for (Auction auctionListing : auctionList) {
            if (auctionListing.getId() == auctionId) {
                System.out.println("Auction Exists");
                if (auctionListing.getFirstBid() < bidAmount) {
                    System.out.println("Higher than First Bid");
                    if (auctionListing.getSellerName().equals(jwtStringName)) {
                        return apiResponse.setResponse(402, "Seller Cannot Bid on Their Own Items");
                    }
                    if (bidList.isEmpty()) {
                        Bid newBid = new Bid(bidAmount, bidderId);
                        newBid.setAuctionId(auctionListing.getId());
                        newBid.setId(1);
                        bidList.add(newBid);
                        return apiResponse.setResponse(1, auctionId, bidAmount, bidderId);
                    }
                    for (Bid bidlisting : bidList) {
                        if (bidlisting.getBidAmount() < bidAmount) {
                            highestBid = bidAmount;
                        } else {
                            return apiResponse.setResponse(403, "Bid Lower Than Current Value");
                        }
                    }
                    Bid newBid = new Bid(bidAmount, bidderId);
                    newBid.setAuctionId(auctionListing.getId());
                    newBid.setBidderId(bidderId);
                    newBid.setId(bidList.size() + 1);
                    bidList.add(newBid);
                    return apiResponse.setResponse(bidList.size(), auctionId, highestBid, bidderId);
                }
                return apiResponse.setResponse(403, "Bid Lower Than Starting Value");
            }
        }
        return apiResponse.setResponse(404, "Auction Not Found");
    }

    public JSONArray listBids(int auctionId) {
        System.out.println("List Bids Method");
        JSONArray jsonResponseFinal = new JSONArray();
        JSONObject jsonResponse = new JSONObject();
        for (Bid bidListing : bidList) {
            if (bidListing.getAuctionId() == auctionId) {
                jsonResponse = apiResponse.setResponse(bidListing.getId(), bidListing.getAuctionId(), bidListing.getBidAmount(),
                        bidListing.getBidderId());
                jsonResponseFinal.put(jsonResponse);
            }
        }
        return jsonResponseFinal;
    }
}
