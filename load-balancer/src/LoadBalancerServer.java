/**
 * Load Balancer Server Refers the Server Object That Information Will Be Routed To
 * @see LoadBalancer
 * @author Andre Obamwonyi
 */
public class LoadBalancerServer {
    private String name;
    private String ip;
    private String status;
    private int commandCount;
    private int id;

    public LoadBalancerServer(String serverName, String serverIp, int id) {
        this.name = serverName;
        this.ip = serverIp;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getCommandCount() {
        return commandCount;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return this.ip;
    }

    public String getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String serverIp) {
        this.ip = serverIp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCommandCount(int commandCount) {
        this.commandCount = commandCount;
    }

    public void setId(int id) {
        this.id = id;
    }
}