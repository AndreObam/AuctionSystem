public class AuctionProperties extends AuctionRequiredProperties {
    private String status;

    public AuctionProperties(){
    }

    /*Method Used to Set the Auction Status*/
    public void setStatus(String auctionStatus){
        this.status = auctionStatus;
    }

    /*Method Used to Get the Auction Status*/
    public String getStatus(){
        return this.status;
    }
}