import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SAvailabilityHandler implements HttpHandler {
    private int commandCount;

    public SAvailabilityHandler(int commandCount){
        this.commandCount = commandCount;
    }

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        is.read();
        t.getResponseHeaders().set("Content-Type", "application/json");
        String endpoint = t.getRequestURI().toString();
        String response = "";

        /*Method Used to Return The The Availability Status of The Server*/
        if (endpoint.equals("/api/availability")) {
            response = "Up";
        } else{
            response = "Down";
        }

        /*Method Used to Return The Amount of Commands Handled By The Server*/
        if (endpoint.equals("/api/availability/commands")) {
            response = Integer.toString(commandCount + 1);
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}