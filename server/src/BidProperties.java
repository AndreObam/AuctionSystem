public class BidProperties {
    private int auctionId;

    public BidProperties() {
    }

    /*Method Used to Set the Bid Id*/
    public void setId(int idValue) {
        this.auctionId = idValue;
    }

    /*Method Used to Get the Bid Id*/
    public int getId() {
        return this.auctionId;
    }
}

