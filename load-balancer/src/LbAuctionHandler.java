import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LbAuctionHandler implements HttpHandler {
    private Client c = new Client();
    private LoadBalancer lb = LoadBalancer.getInstance();
    private List<LoadBalancerServer> servers = lb.getServers();

    public List<LoadBalanceReq> getCommandList() {
        return lb.getCommandList();
    }

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        is.read();
        t.getResponseHeaders().set("Content-Type", "application/json");
        String requestMethod = t.getRequestMethod();
        String endpoint = t.getRequestURI().toString();
        String response = "This is the response";

        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        String requestBody = buf.toString();

        long millis = System.currentTimeMillis();
        int time=(int)millis;

        /*Place Bid*/
        if (endpoint.contains("/api/auction/") && endpoint.contains("/bid") && requestMethod.equals("POST")) {
            try {
                System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
                System.out.println(requestBody);
                Pattern pattern = Pattern.compile("(\"[a-zA-Z:\"]{12})([0-9]+)([a-zA-Z,:\"]{14})([0-9]+)");
                Matcher matcher = pattern.matcher(requestBody);
                int auctionId = Integer.parseInt((endpoint.substring(13)).substring(0, (endpoint.substring(13)).indexOf('/')));
                System.out.println(auctionId);
                JSONObject addCredentials = new JSONObject();
                while (matcher.find()) {
                    addCredentials.put("bidAmount", matcher.group(2));
                    addCredentials.put("bidderId", matcher.group(4));
                }
                List<String> jwtList = t.getRequestHeaders().get("Authorization");
                String jwtToken = jwtList.toString();
                jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
                lb.addCommand(new LoadBalanceReq(jwtToken, requestMethod, endpoint, addCredentials.toString(), time));
                for (int server = 0; server < lb.getServers().size(); server++) {
                    try {
                        response = c.postJWT(servers.get(server).getIp() + "/api/auction/" + auctionId + "/bid", addCredentials.toString(), jwtToken);
                    } catch (Exception e) {
                        response = c.postJWT(servers.get(server).getIp() + "/api/auction/" + auctionId + "/bid", addCredentials.toString(), jwtToken);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*List All Bid*/
        else if (endpoint.contains("/api/auction/") && endpoint.contains("/bids") && requestMethod.equals("GET")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            List<String> jwtList = t.getRequestHeaders().get("Authorization");
            String jwtToken = jwtList.toString();
            jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
            lb.addCommand(new LoadBalanceReq(jwtToken, requestMethod, endpoint, null, time));
            try {
                response = c.getJWT((lb.roundRobin() + endpoint), jwtToken);
            } catch (Exception e) {
                response = c.getJWT((lb.roundRobin() + endpoint), jwtToken);
            }
        }

        /*List All Auctions - GET*/
        else if (endpoint.equals("/api/auctions")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            lb.addCommand(new LoadBalanceReq(null, requestMethod, endpoint, null, time));
            try {
                response = c.get(lb.roundRobin() + "/api/auctions");
            } catch (Exception e) {
                response = c.get(lb.roundRobin() + "/api/auctions");
            }
        }

        /*Add An Auction*/
        else if (endpoint.equals("/api/auction")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            String test = requestBody.replaceAll("[:\",]", "");
            Pattern pattern = Pattern.compile("([a-zA-Z]{8})([0-9]+)([a-zA-Z]{8})([0-9]+)([a-zA-Z]{4})([a-zA-Z\\s]+)");
            Matcher matcher = pattern.matcher(test);
            JSONObject addCredentials = new JSONObject();
            while (matcher.find()) {
                addCredentials.put("postName", matcher.group(6));
                addCredentials.put("postBid", matcher.group(4));
                addCredentials.put("postId", matcher.group(2));
            }
            List<String> jwtList = t.getRequestHeaders().get("Authorization");
            String jwtToken = jwtList.toString();
            jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
            lb.addCommand(new LoadBalanceReq(jwtToken, requestMethod, endpoint, addCredentials.toString(), time));
            for (int server = 0; server < lb.getServers().size(); server++) {
                try {
                    response = c.postJWT((servers.get(server).getIp() + "/api/auction"), addCredentials.toString(), jwtToken);
                } catch (Exception e) {
                    response = c.postJWT((servers.get(server).getIp() + "/api/auction"), addCredentials.toString(), jwtToken);
                }
            }
        }

        /*Find An Auction By ID* - GET*/
        else if (endpoint.contains("/api/auction/") && requestMethod.equals("GET")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            int auctionId = Integer.parseInt(endpoint.substring(13));
            lb.addCommand(new LoadBalanceReq(null, requestMethod, endpoint, null, time));
            try {
                response = c.get((lb.roundRobin() + "/api/auction/" + auctionId));
            } catch (Exception e) {
                response = c.get((lb.roundRobin() + "/api/auction/" + auctionId));
            }
        }

        /*Update An Auction*/
        else if (endpoint.contains("/api/auction") && requestMethod.equals("POST")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            String test = requestBody.replaceAll("[:\",]", "");
            int auctionId = Integer.parseInt(endpoint.substring(13));
            Pattern pattern = Pattern.compile("([a-zA-Z]{8})([0-9]+)([a-zA-Z]{8})([0-9]+)([a-zA-Z]{4})([a-zA-Z\\s]+)");
            Matcher matcher = pattern.matcher(test);
            JSONObject updateDetails = new JSONObject();
            while (matcher.find()) {
                updateDetails.put("postName", matcher.group(6));
                updateDetails.put("postBid", matcher.group(4));
                updateDetails.put("postId", matcher.group(2));
            }
            List<String> jwtList = t.getRequestHeaders().get("Authorization");
            String jwtToken = jwtList.toString();
            jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
            lb.addCommand(new LoadBalanceReq(jwtToken, requestMethod, endpoint, updateDetails.toString(), time));
            for (int server = 0; server < lb.getServers().size(); server++) {
                try {
                    response = c.postJWT((servers.get(server).getIp() + "/api/auction/" + auctionId), updateDetails.toString(), jwtToken);
                } catch (Exception e) {
                    response = c.postJWT((servers.get(server).getIp() + "/api/auction/" + auctionId), updateDetails.toString(), jwtToken);
                }
            }
        }

        // Delete Method
        else if (endpoint.contains("/api/auction/") && requestMethod.equals("DELETE")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            int auctionId = Integer.parseInt(endpoint.substring(13));
            List<String> jwtList = t.getRequestHeaders().get("Authorization");
            String jwtToken = jwtList.toString();
            jwtToken = jwtToken.substring(0, jwtToken.length() - 1).replaceAll("\\[Bearer ", "");
            lb.addCommand(new LoadBalanceReq(jwtToken, requestMethod, endpoint, null, time));
            for (int server = 0; server < lb.getServers().size(); server++) {
                try {
                    response = c.deleteJWT(((servers.get(server).getIp() + "/api/auction/" + auctionId)), jwtToken);
                } catch (Exception e) {
                    response = c.deleteJWT(((servers.get(server).getIp() + "/api/auction/" + auctionId)), jwtToken);
                }
            }
        }

        t.sendResponseHeaders(302, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}