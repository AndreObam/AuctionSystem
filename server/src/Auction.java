public class Auction extends AuctionId  {
    private int id;
    private String status;
    private String name;
    private float firstBid;
    private int sellerId;
    private String sellerName;

    public Auction(){
        this.name = "Available";
    }

    public void setId(int auctionId){
        this.id = auctionId;
    }

    public void setStatus(String auctionStatus){
        this.status = auctionStatus;
    }

    public void setName(String auctionName){
        this.name = auctionName;
    }

    public void setFirstBid(float auctionFirstBid){
        this.firstBid = auctionFirstBid;
    }

    public void setSellerId(int auctionSellerId){
        this.sellerId = auctionSellerId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public int getId(){
        return this.id;
    }

    public String getStatus(){
        return this.status;
    }

    public String getName(){
        return this.name;
    }

    public float getFirstBid(){
        return this.firstBid;
    }

    public int getSellerId(){
        return this.sellerId;
    }
}