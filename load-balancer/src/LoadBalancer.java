import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import java.net.InetSocketAddress;
import java.util.*;

public class LoadBalancer {

  private static LoadBalancer instance;
  private List<LoadBalancerServer> servers = new ArrayList<LoadBalancerServer>();
  private List<LoadBalanceReq> commandList = new ArrayList<>();

  public LoadBalancer() {
    try {
      Jedis jedis = new Jedis("localhost");
      jedis.lpush("jServerList", "http://localhost:8081");
      jedis.lpush("jServerList", "http://localhost:8080");
      List<String> serverList = jedis.lrange("jServerList", 0, 1);
      for (int i = 0; i < serverList.size(); i++) {
        servers.add(new LoadBalancerServer("Redis Instance " + Integer.toString(i + 1), serverList.get(i), 0));
      }
      System.out.println("Redis Currently Operating");
    } catch(Exception e){
      servers.add(new LoadBalancerServer("Redis Instance A", "http://localhost:8080", 0));
      servers.add(new LoadBalancerServer("Redis Instance B", "http://localhost:8081", 0));
      System.out.println("Redis Not Operating");
    }
  }

  public String roundRobin(){
    if(0 == servers.get(0).getId()){
      servers.get(0).setId(1);
      System.out.println("Round Robin Server 1: " +servers.get(0).getIp());
      return servers.get(0).getIp();
    } else{
      servers.get(0).setId(0);
      System.out.println("Round Robin Server 2: " +servers.get(1).getIp());
      return servers.get(1).getIp();
    }
  }

  public static LoadBalancer getInstance() {
    if (instance == null) {
      instance = new LoadBalancer();
    }
    return instance;
  }

  public List getServers() {
    return this.servers;
  }

  public List<LoadBalanceReq> getCommandList() {
    return commandList;
  }

  public void addCommand(LoadBalanceReq request) {
    commandList.add(request);
  }

  public static void restoreData(String ipAddress, List<LoadBalanceReq> requestList) throws Exception {
    System.out.println("Restoration Process For " + ipAddress);
    Client c = new Client();
    String endpoint = "";
    String requestMethod = "";
    System.out.println("Cached Command List Size: " + requestList.size());

    for (int counter = 0; counter < requestList.size(); counter++) {
      endpoint = requestList.get(counter).getUrl();
      requestMethod = requestList.get(counter).getRequestType();
       if (endpoint.contains("/api/auction/") && endpoint.contains("/bid") && requestMethod.equals("POST")) {
        System.out.println("Place Bid");
        c.postJWT(ipAddress + endpoint, requestList.get(counter).getJson(), requestList.get(counter).getJwtToken());
      } else if (endpoint.equals("/api/auction")) {
        System.out.println("Add Auction");
        c.postJWT(ipAddress + endpoint, requestList.get(counter).getJson(), requestList.get(counter).getJwtToken());
      } else if (endpoint.equals("/api/user")) {
        System.out.println("Create User");
        c.post(ipAddress + endpoint, requestList.get(counter).getJson());
      } else if (endpoint.equals("/api/user/login")) {
        System.out.println("Login User");
        c.post(ipAddress + endpoint, requestList.get(counter).getJson());
      } else if (endpoint.contains("/api/auction") && requestMethod.equals("POST")) {
        System.out.println("Update Auction");
        c.postJWT(ipAddress + endpoint, requestList.get(counter).getJson(), requestList.get(counter).getJwtToken());
      } else if (endpoint.contains("/api/auction/") && requestMethod.equals("DELETE")) {
        System.out.println("Delete Auction");
        c.deleteJWT(ipAddress + endpoint, requestList.get(counter).getJwtToken());
      }
    }
    System.out.println("Server " + ipAddress + " Is Now Restored");
  }

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
    server.createContext("/api/auction", new LbAuctionHandler());
    server.createContext("/api/user", new LbUserHandler());

    server.start();
    System.out.println("Load Balancer Is Currently Listening!\n");
    Server s = new Server();
    Client c = new Client();
    LoadBalancer lb = new LoadBalancer();
    LbAuctionHandler lbAuctionHandler = new LbAuctionHandler();


    Timer t = new Timer();
    t.scheduleAtFixedRate(new TimerTask() {

      @Override
      public void run() {
          for (int server = 0; server < lb.getServers().size(); server++) {
              try {
                  c.get(lb.servers.get(server).getIp());
                  System.out.println("Periodic Server Availalibility: " + lb.servers.get(server).getIp() + " is Up");
              } catch (Exception e) {
                  System.out.println("Periodic Server Availalibility: " + lb.servers.get(server).getIp() + " is Down");
                  lb.servers.get(server).setStatus("Down");
              }
          }
          for (int server = 0; server < lb.getServers().size(); server++) {
              try {
                  if ((lb.servers.get(server).getStatus().equals("Down")) && (c.get(lb.servers.get(server).getIp() + "/api/availability").equals("Up"))) {
                      try {
                          Jedis jedis = new Jedis("localhost");
                          JSONObject jsonCommands = new JSONObject(lbAuctionHandler);
                          jedis.lpush("cachedCommands", jsonCommands.toString());
                          List<String> commandList = jedis.lrange("cachedCommands", 0, 1);
                          System.out.println("Jedis Cached Restore Commands:");
                          System.out.println(commandList);
                          System.out.println("Non-Jedis Restore");
                          restoreData(lb.servers.get(server).getIp(), lbAuctionHandler.getCommandList());
                          lb.servers.get(server).setStatus("Up");
                      } catch (Exception e) {
                          System.out.println("Non-Jedis Restore");
                          restoreData(lb.servers.get(server).getIp(), lbAuctionHandler.getCommandList());
                          lb.servers.get(server).setStatus("Up");
                      }
                  }
              } catch (Exception e){
              }
          }
      }
    }, 1000, 8000);
  }
}