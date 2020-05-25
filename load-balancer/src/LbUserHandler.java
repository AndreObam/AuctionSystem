import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LbUserHandler implements HttpHandler {
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

        /*Create User*/
      if (endpoint.equals("/api/user")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            Pattern pattern = Pattern.compile("([a-zA-Z\":\"]{12})([0-9a-zA-Z:+]+)(\",\")([0-9a-zA-Z:\"+]{11})([0-9a-zA-Z]+)");
            Matcher matcher = pattern.matcher(requestBody);
            JSONObject registerCredentials = new JSONObject();
            while (matcher.find()) {
                registerCredentials.put("username", matcher.group(5));
                registerCredentials.put("password", matcher.group(2));
            }
            lb.addCommand(new LoadBalanceReq(null, requestMethod,endpoint, registerCredentials.toString(), time));
            for(int server = 0; server < lb.getServers().size(); server++){
                try {
                    response = c.post((servers.get(server).getIp() + "/api/user"),registerCredentials.toString());
                } catch(Exception e){
                    System.out.println("Server Down: " + servers.get(server).getIp());
                }
            }
        }
        /*Log User Into the System*/
        else if (endpoint.equals("/api/user/login")) {
            System.out.println("Method: " + t.getRequestMethod() + "  End Point:" + t.getRequestURI());
            Pattern pattern = Pattern.compile("([a-zA-Z\":\"]{12})([0-9a-zA-Z:+]+)(\",\")([0-9a-zA-Z:\"+]{11})([0-9a-zA-Z]+)");
            Matcher matcher = pattern.matcher(requestBody);
            JSONObject loginCredentials = new JSONObject();
            while (matcher.find()) {
                loginCredentials.put("username", matcher.group(5));
                loginCredentials.put("password", matcher.group(2));
            }
            lb.addCommand(new LoadBalanceReq(null, requestMethod,endpoint, loginCredentials.toString(), time));
            for(int server = 0; server < lb.getServers().size(); server++){
                try {
                    response = c.post((servers.get(server).getIp() + "/api/user/login"),loginCredentials.toString());
                } catch(Exception e){
                    System.out.println("Server Down: " + servers.get(server).getIp());
                }
            }
        }
        t.sendResponseHeaders(302, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}