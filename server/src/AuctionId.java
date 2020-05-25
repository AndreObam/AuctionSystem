public class AuctionId extends AuctionProperties{
    private int id;

    public AuctionId(){
    }

    /*Method Used to Set the Auction Id*/
    public void setId(int auctionId){
        this.id = auctionId;
    }

    /*Method Used to Get the Auction Id*/
    public int getId(){
        return this.id;
    }
}