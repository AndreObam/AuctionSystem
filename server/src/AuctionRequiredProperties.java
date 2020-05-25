public class AuctionRequiredProperties {
    private String name;
    private float firstBid;
    private int sellerId;

    public AuctionRequiredProperties(){ }

    /*Method Used to set Name*/
    public void setName(String auctionName){
        this.name = auctionName;
    }

    /*Method Used to set First Bid Id*/
    public void setFirstBid(float auctionFirstBid){
        this.firstBid = auctionFirstBid;
    }

    /*Method Used to Set Seller Id*/
    public void setSellerId(int auctionSellerId){
        this.sellerId = auctionSellerId;
    }

    /*Method Used to get Name*/
    public String getName(){
        return this.name;
    }

    /*Method Used to get the First Bid*/
    public float getFirstBid(){
        return this.firstBid;
    }

    /*Method Used to get the Seller Id*/
    public int getSellerId(){
        return this.sellerId;
    }
}