public class BidRequiredProperties extends BidProperties {
    private float bidAmount;
    private int bidderId;

    public BidRequiredProperties(/*float bidAmountValue, int bidderIdVal*/) {
        //this.bidAmount = bidAmountValue;
        //this.bidderId = bidderIdValue;
    }
    /*Method Used to Set the Id*/
    public void setId(int idValue) {
        this.bidderId = idValue;
    }

    /*Method Used to Set the Bidder Id*/
    public void setBidderId(int bidderIdValue) {
        this.bidderId = bidderIdValue;
    }

    /*Method Used to Get the Bid Id*/
    public int getId() {
        return this.bidderId;
    }

    /*Method Used to Get the Bidder Id*/
    public int getBidderId() {
        return this.bidderId;
    }

}

